package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;

import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import twitter4j.TwitterException;

public abstract class TwitterSample extends Sample {
	protected TwitterRequestHandler t = new TwitterRequestHandler();
	long[] open = TwitterAuth.open;
	boolean fsleep;
	boolean usleep;
	boolean ssleep;
	boolean[] sleep = { ssleep, usleep, fsleep };

	public TwitterSample() {
		this.getFollowingQ = new LinkedList<ToFollow>();
		this.getPostsQ = new LinkedList<TwitterUser>();
		this.getUserQ = new LinkedList<ToUser>();
	}

	@Override
	boolean completed() {
		if (this.getFollowingQ.size() == 0 && this.getPostsQ.size() == 0 && this.getUserQ.size() == 0)
			return true;
		return false;
	}

	static String[] fams = { "Status", "User", "Following" };

	private boolean sleeping(int i) {

		if (!sleep[i])
			return false;
		else {
			long now = java.lang.System.currentTimeMillis();
			if (now > open[i]) {
				sleep[i] = false;
				System.out.println(fams[i] + " family is resuming queries.");
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	boolean followingSleeping() {
		return sleeping(2);
	}

	@Override
	boolean postSleeping() {
		return sleeping(0);
	}

	@Override
	boolean userSleeping() {
		return sleeping(1);
	}

	@Override
	public ToUser getFol(ToFollow u) {
		try {
			return TwitterRequestHandler.getFollowers(u);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		} catch (BadIDException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	public ToUser getFriends(ToFollow u) {
		try {
			return TwitterRequestHandler.getFriends(u);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		} catch (BadIDException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	@Override
	public User getUser(ToUser id) {
		try {
			User u = TwitterRequestHandler.getUser(Long.parseLong(id.id), id.depth);
			return u;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (BadUserException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	@Override
	public User[] getUsers(ToUser toU) {
		if (toU.single) {
			User[] out = new User[1];
			out[0] = getUser(toU);
			return out;
		} else
			return getUsersFromMultiple(toU);
	}

	private User[] getUsersFromMultiple(ToUser ids) {
		long[] idsNum = new long[ids.ids.length];
		int i = 0;
		for (String id : ids.ids) {
			idsNum[i] = Long.parseLong(id);
			i++;
		}
		if (ids.ids.length > 100) { // should only be given lists of less than
									// 100!!!!!
			System.err.println(
					"Bad list of IDs to getUsers. Max depth is 100 and this input had a depth of " + ids.ids.length);
			System.err.println("Attempting to return the first 100 users.");
			return TwitterRequestHandler.getUsers(Arrays.copyOf(idsNum, 100), ids.depth);
		} else
			return TwitterRequestHandler.getUsers(idsNum, ids.depth);
	}

	@Override
	public void getPosts(User u) {
		try {
			TwitterRequestHandler.getPosts(u);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.err.println("continuing data collection.");
		}
	}

	class Listener implements Ratelimit_Reached_Listener {
		@Override
		public void reached(int i) {
			switch (i) {
			case 0:
				ssleep = true;
				break;
			case 1:
				usleep = true;
				break;
			case 2:
				fsleep = true;
				break;
			default:
				System.err.println("Weird value passed to reached() method in TwitterSample.Listener");
			}
		}
	}

	// directly save the incoming tweets and users as they come in imo!

	public void usersToTSV() {
		PrintWriter w = fileHandler(name + "_users.tsv");
		w.println("~users~");
		Enumeration<String> keys = users.keys();
		while (keys.hasMoreElements()) {
			w.println(users.get(keys.nextElement()));
		}
		w.close();
	}

	public void followsToTSV() {
		PrintWriter w = fileHandler(name + "_follows.tsv");
		w.println("~follows~");
		for (Follow follow : follows) {
			w.println(follow);
		}
		w.close();
	}

	public void postsToTSV() {
		PrintWriter w = fileHandler(name + "_posts.tsv");
		w.println("~posts~");
		for (String k : posts.keySet()) {
			w.println(posts.get(k));
		}
		w.close();
	}

	// TODO
	public void loadFromTSV(String dir) {
		File p = new File(Paths.get(dir).toString()); // in case the input path
														// isn't absolute
		File[] directory = p.listFiles();
		if (directory.length == 0) {
			System.err.println("Empty path passed to loadFromCSV: " + dir);
			System.err.println("Qutting.");
			System.exit(0);
		}
		ArrayList<Fwrap> sorted = new ArrayList<Fwrap>();
		for (File file : directory) {
			Fwrap w = new Fwrap(file);
			if (w.f != null && w.ordering < 5) {
				sorted.add(w);
			}
		}
		Collections.sort(sorted);
		for (Fwrap w : sorted) {
			File file = w.f;
			System.out.println("found file: " + file.getName());
			try {
				Scanner s = new Scanner(file);
				if (s.hasNextLine()) {
					switch (s.nextLine()) {
					case "~follows~":
						importFols(s);
						break;
					case "~posts~":
						importPosts(s);
						break;
					case "~users~":
						importUsers(s);
						break;
					case "~interactions~":
						importInteractions(s);
						break;
					default:
						System.out.println("File " + file.getName() + " was not incuded.");
					}
					System.out.println("Finished with file " + file.getName());
				} else {
					System.out.println("File " + file.getName() + " has no content and was not included.");
				}
				s.close();
			} catch (FileNotFoundException e) {
				System.err.println("File deleted or renamed during operation– " + file.getName());
				System.err.println("File " + file.getName() + " can not be read. Quitting program.");
				System.exit(0);
			}
		}
	}

	// TODO
	private void importFols(Scanner s) {
		while (s.hasNextLine()) {
			new Follow(s.nextLine());
		}
	}

	// TODO
	private void importPosts(Scanner s) {
		while (s.hasNextLine()) {
			new TwitterStatus(s.nextLine());
		}
	}

	// TODO
	private void importUsers(Scanner s) {
		while (s.hasNextLine()) {
			new TwitterUser(s.nextLine());
		}
	}

	// TODO
	private void importInteractions(Scanner s) {
		if (this.follows != null && this.follows.size() > 0) {
			while (s.hasNextLine()) {
				String cur = s.nextLine();
				String[] split = cur.split("\t");
				switch (split[2]) {
				case "follow":
					break; // don't reimport follow objects!
				case "like":
					new Like(cur);
					break; // TODO add support for this feature!
				case "repost":
					new Repost(cur);
					break;
				case "comment":
					new Comment(cur);
					break; // TODO add support for this feature!
				}
			}
		} else {
			while (s.hasNextLine()) {
				String cur = s.nextLine();
				String[] split = cur.split("\t");
				switch (split[3]) {
				case "follow":
					new Follow(cur);
					break;
				// case "like": new Like(cur); break;
				case "repost":
					new Repost(cur);
					break;
				// case "comment": new Comment(cur); break;
				}
			}
		}
	}

}

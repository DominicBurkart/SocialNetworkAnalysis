package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import twitter4j.TwitterException;

/**
 * Implements methods useful specifically for twitter samples and fleshes
 * out stuff established as important in sample. Wraps TwitterRequestHandler
 * queries and holds the boolean values of whether or not a request family is sleeping.
 * 
 * @author dominicburkart
 */
public abstract class TwitterSample extends Sample {
	protected TwitterRequestHandler t = new TwitterRequestHandler();
	static long[] open = new long[3];
	static boolean[] sleep = new boolean[3];
	static  Hashtable<String, LinkedList<User>> toLinkFriends = new Hashtable<String,LinkedList<User>>();

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

	private boolean sleeping(int i) {
		if (verbose) System.out.println("Checking if resource #"+i+" is available for queries.");
		if (!sleep[i]){
			if (verbose) System.out.println("resource #"+i+" is not asleep and thus is available for queries.");
			return false;
		}
		else {
			if (verbose) System.out.println("resource #"+i+" was set to asleep. Checking to see if resource can be woken up.");
			long now = java.lang.System.currentTimeMillis();
			if (now > open[i]) {
				if (verbose) System.out.println("TwitterSample.sleeping: current time "+now +" is larger than projected open time "+open[i]+", indicating that the resource can be woken up.");
				sleep[i] = false;
				return false;
			} else {
				if (verbose) System.out.println("TwitterSample.sleeping: current time "+now +" is smaller than projected open time "+open[i]+", indicating that the resource cannot be woken up.");
				return true;
			}
		}
	}
	
	
	/**
	 * @return a linked list with the indexes of the active (non-empty) queues.
	 */
	private LinkedList<Integer> activeQueues(){
		LinkedList<Integer> active = new LinkedList<Integer>();
		if (getPostsQ.size() > 0) active.add(0);
		if (getUserQ.size() > 0) active.add(1);
		if (getFollowingQ.size() > 0) active.add(2);
		return active;
	}
	
	/**
	 * sleeps the program when no resources with non-empty queues are awake until one wakes up.
	 */
	@Override
	public void filler(){
		LinkedList<Integer> active = activeQueues();
		for (int relevantIndex : active){
			if (!sleep[relevantIndex]) return; //a resource is open and has waiting queries! 
		}
		// if we make it here then the active resources are all alseep. let's figure out how long to wait.
		long[] relevantOpens = new long[active.size()];
		int i = 0;
		for (int relevantIndex : active){
			relevantOpens[i++] = open[relevantIndex];
		}
		try{
			long sleep = Utilities.least(relevantOpens) - java.lang.System.currentTimeMillis();
			if (sleep <= 0) return;
			//okay, we know that we have to wait and for how long. Save current data and go to sleep.
			System.out.println("All relevant resources are asleep. Sleeping program for "+sleep/1000+" seconds while waiting for the next resource to wake up.");
			Date d = new Date();
			System.out.println("Current time: "+d.toString());
			if (sleep > 1000 * 60 * 14){
				//only save files if we're sleeping for more than fourteen minutes.
				toTSV();
				sleep = Utilities.least(relevantOpens) - java.lang.System.currentTimeMillis();
				if (sleep <= 0) return;
				//recalculate sleep after saving all of those files.
			}
			System.out.println("Waking at: "+ Utilities.durationToTimeString(sleep));
			try {
				Thread.sleep(sleep);
				System.out.println("Awake! Continuing collection.");
			} catch (InterruptedException e) {
				System.err.println("System could not sleep for designated period. Continuing program.");
			}
		} catch (IllegalArgumentException e){ //thrown by Utilities.least method
			if (verbose) System.out.println("All queues are empty. Exiting filler method.");
			return;
		}
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
	boolean followingSleeping() {
		return sleeping(2);
	}

	@Override
	public ToUser getFol(ToFollow u) {
		try {
			return TwitterRequestHandler.getSomeFollowers(u);
		} catch (TwitterException | BadIDException e) {
			System.out.println("TwitterSample.getFol problem");
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
		if (ids.ids.length > 100) {
			Utilities.toUserChunker(ids); //splits the overly large toUser object and puts the chunks onto the correct queue.
			return getUsers(getUserQ.poll()); //returns null since it added all of the values it was going to query to its own queue to query later.
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

	public static class Listener implements Ratelimit_Reached_Listener {
		@Override
		public void reached(int i) {
			if (verbose){
				System.out.println("Reached method called in TwitterSample.Listener for family: "+i);
			}
			if (i >= sleep.length){
				System.err.println("Bad value passed to TwitterSample's Listener class: "+i);
				System.err.println("Sleep size in TwitterSample is only "+sleep.length);
				System.err.println("Quitting without saving.");
				System.exit(0);
			}
			else if (i < 0){
				System.err.println("Bad value passed to TwitterSample's Listener class: "+i);
				System.err.println("Value should correspond to a bool index in the datafield 'sleep'.");
				System.err.println("Quitting without saving.");
				System.exit(0);
			}
			sleep[i] = true;
			open[i] = java.lang.System.currentTimeMillis() + (16 * 60 * 1000);
			if (verbose){
				System.out.print("Values for sleep after update in TwitterSample.Listener: ");
				for (boolean s : sleep){
					System.out.print(Boolean.toString(s)+" ");
				}
				System.out.println();
				System.out.print("Values for open after update in TwitterSample.Listener: ");
				for (long o : open){
					System.out.print(o+" ");
				}
				System.out.println();
			}
		}
	}

	//TODO directly save the incoming tweets and users as they come in imo!

	@Override
	public void usersToTSV() {
		///File userDirectory = new File(name+"users");
		PrintWriter w = fileHandler(name + "_users.tsv"); //for master user file
		w.println("~users~");
		Enumeration<String> keys = users.keys();
		while (keys.hasMoreElements()) { //adds a line per user to the master user file and also makes baby files for each user
			User u = users.get(keys.nextElement());
			String n = name+"_user_"+u.id+"_"; //for naming files
			w.println(u);
			if (u.getTensors().friends != null && u.getTensors().friends.size() > 0){
				PrintWriter friends = fileHandler(n+"friends.tsv");
				for (Follow fol : u.getTensors().friends){
					friends.println(fol.target);
				}
				friends.close();
			}
			if (u.posts != null && u.posts.size() > 0){
				PrintWriter posts = fileHandler(n+"posts.tsv");
				for (Post p : u.posts){
					posts.println(p);
				}
				posts.close();
			}
		}
		w.close();
	}

	@Override
	public void followsToTSV() {
		PrintWriter w = fileHandler(name + "_follows.tsv");
		w.println("~follows~");
		for (Follow follow : follows) {
			w.println(follow);
		}
		w.close();
	}

	@Override
	public void postsToTSV() {
		PrintWriter w = fileHandler(name + "_posts.tsv");
		w.println("~posts~");
		for (String k : posts.keySet()) {
			w.println(posts.get(k));
		}
		w.close();
	}

	// TODO
	@Override
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

	public ToUser getSomeFol(ToFollow f, int some) {
		try {
			return TwitterRequestHandler.getxFollowers(f, some);
		} catch (TwitterException e) {
			System.out.println("TwitterSample.getFol problem");
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

}

package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Abstract class to refer to a data collecion. Provides Framework to 
 * perform iterative behaviors and optimizes API usage given ratelimiting.
 * 
 * @author dominicburkart
 */
public abstract class Sample extends SNA_Root {
	Hashtable<String, Post> posts = new Hashtable<String, Post>();
	public Hashtable<String, User> users = new Hashtable<String, User>();
	LinkedList<Follow> follows = new LinkedList<Follow>();
	ArrayList<Interaction> allInteractions = new ArrayList<Interaction>();
	String outDir;
	public String name = "collection";

	public Sample() {
		User.sample = this;
		Interaction.sample = this;
		Post.sample = this;
	}

	/**
	 * Determines whether there is still more data to collect.
	 * 
	 * @return true when the collection is sufficient
	 */
	abstract boolean completed();

	/**
	 * Checks if we can query for followers.
	 * 
	 * @return true if the resource is currently unaccessible.
	 */
	abstract boolean followingSleeping();

	/**
	 * Checks if we can query for a user's posts.
	 * 
	 * @return true if the resource is currently unaccessible.
	 */
	abstract boolean postSleeping();

	/**
	 * Checks if we can query for a user profile.
	 * 
	 * @return true if the resource is currently unaccessible.
	 */
	abstract boolean userSleeping();

	public Queue<ToFollow> getFollowingQ;
	public Queue<TwitterUser> getPostsQ; // TODO make this more portable by creating a GetPost class to store the User
	public Queue<ToUser> getUserQ;

	/**
	 * Returns true if we should do the defined userAction to the user.
	 */
	public abstract boolean userConditions(User u);

	public abstract void userAction(User u);

	/**
	 * Returns true if we should do the defined followAction to the list of IDs.
	 */
	public abstract boolean followingConditions(ToUser ids);

	public abstract void followAction(ToUser ids);

	/**
	 * Seed of data collection (eg, when constructing a network of Hillary
	 * Clinton's twitter followers, start() collects the twitter account of
	 * Hillary Clinton).
	 */
	public abstract void start();

	/**
	 * Get the ids of a user's followers in a handy ToUser object!
	 */
	public abstract ToUser getFol(ToFollow toFol);

	/**
	 * Get a user from an ID!
	 * 
	 * @see getUsers
	 */
	public abstract User getUser(ToUser id);

	/**
	 * Get a collection of users from a list of IDs.
	 * 
	 * Assumes that all users have the same depth, which you assign to them when
	 * you make the ToUser object passed to this method.
	 */
	public abstract User[] getUsers(ToUser ids);

	/**
	 * Get posts from a user!
	 */
	public abstract void getPosts(User u);

	/**
	 * Runs the data collection.
	 */
	public void run() {
		start();
		System.out.println("Beginning run() while loop.");
		long it = 0;
		while (!completed()) {
			if (verbose) System.out.println("current run while loop iteration: "+it++);
			if (!getFollowingQ.isEmpty() && !followingSleeping()) {
				System.out.println("following request in run()!");
				ToFollow parent = getFollowingQ.poll();
				ToUser babies = getFol(parent);
				if (followingConditions(babies))
					followAction(babies);
			}
			if (!getUserQ.isEmpty() && !userSleeping()) {
				System.out.println("user request in run()!");
				ToUser account = getUserQ.poll();
				System.out.print("Account (from Sample.run()'s getUser condition): ");
				System.out.print("account is single: "+account.single+" ");
				System.out.print("id(s):");
				if (account.single){
					System.out.println(" "+account.id);
				}
				else{
					for (String id: account.ids){
						System.out.print(" "+id);
					}
				}
				System.out.println();
				if (account.single) {
					User u = getUser(account);
					if (userConditions(u))
						userAction(u);
				} else {
					User[] us = getUsers(account); 
					for (User u : us) {
						if (userConditions(u))
							userAction(u);
					}
				}
			}
			if (!getPostsQ.isEmpty() && !postSleeping()) {
				System.out.println("post request in run()!");
				getPosts(getPostsQ.poll());
			}
			if (verbose == true) {
				System.out.print("Q lengths: ");
				for (int length : QueueLengths()) {
					System.out.print(length + " ");
				}
				System.out.println();
			}
		}
		System.out.println("Iterative collection completed.");
		finish();
	}

	public void finish() {
		System.out.println("Running sample.finish(). Saving data.");
		toTSV();
	}

	public int[] QueueLengths() {
		int[] sizes = { getFollowingQ.size(), getUserQ.size(), getPostsQ.size() };
		return sizes;
	}

	public class ToUser {
		public boolean single;
		public String id;
		public String[] ids;
		public int depth;

		public ToUser(String id, int depth) {
			this.id = id;
			this.depth = depth;
			this.single = true;
		}

		public ToUser(String[] ids, int depth) {
			this.ids = ids;
			this.depth = depth;
			this.single = false;
		}
	}

	public class ToFollow {
		String id; // user to follow
		long cursor = -1;
		int depth; // depth of user we have the id of.

		public ToFollow(User u, long cursor) {
			this(u);
			this.cursor = cursor;
		}

		public ToFollow(User u) {
			this.id = u.id;
			this.depth = u.firstDepth;
		}

		public ToFollow(String id, int depth, long cursor) {
			this(id, depth);
			this.cursor = cursor;
		}

		public ToFollow(String id, int depth) {
			this.id = id;
			this.depth = depth;
		}
	}

	public void toTSV() {
		interactionsToTSV();
		usersToTSV();
		followsToTSV();
		postsToTSV();
	}

	public void toTSV(String outDir) {
		this.outDir = outDir;
		interactionsToTSV(); // saving order is arbitrary
		usersToTSV();
		followsToTSV();
		postsToTSV();
	}

	public void interactionsToTSV() {
		PrintWriter w = fileHandler(name + "_interactions.tsv");
		w.println("~interactions~");
		for (int i = 0; i < allInteractions.size(); i++) {
			w.println(allInteractions.get(i));
		}
		w.close();
	}

	abstract void usersToTSV();

	abstract void followsToTSV();

	abstract void postsToTSV();

	public void loadFromTSV() {
		String dir = System.getProperty("user.dir");
		System.out.println("load from csv()'s dir: " + dir);
		loadFromTSV(dir);
	}

	public class Fwrap implements Comparable<Fwrap> {
		String n;
		File f;
		int ordering;

		public Fwrap(File f) {
			if (f.getName().endsWith(".tsv")) {
				this.f = f;
				this.n = f.getName();
				defOrd();
			} else {
				System.out.println("Discluding file " + f.getName());
			}
		}

		private void defOrd() {
			if (n.endsWith("_users.tsv")) {
				ordering = 1;
			} else if (n.endsWith("_posts.tsv")) { //  dependent on loading users
				ordering = 2;
			} else if (n.endsWith("_follows.tsv")) { // dep on users
				ordering = 3;
			} else if (n.endsWith("_interactions.tsv")) { //dep on posts, follows
				ordering = 4;
			} else {
				ordering = 5; // all other files will ordered as 5.
				System.out.println("Discluding file " + n);
			}
		}

		@Override
		public int compareTo(Fwrap o) {
			return this.ordering - o.ordering;
		}
	}

	abstract void loadFromTSV(String dir);

	public PrintWriter fileHandler(String fname) {
		checkTestOut();
		File f;
		if (outDir != null || outDir != "") {
			Path p = Paths.get(outDir);
			p = p.toAbsolutePath();
			f = new File(p + "/" + fname);
		} else {
			f = new File(fname);
		}
		PrintWriter out;
		try {
			out = new PrintWriter(f);
			return out;
		} catch (FileNotFoundException e) {
			System.err.println("Sample.CheckTestOut() failed.");
			return null;
		}
	}

	public void checkTestOut() {
		if (outDir == null) {
			outDir = "";
			System.out.println("Saving files in the project folder.");
		} else if (outDir != "") {
			File f;
			try {
				Path p = Paths.get(outDir);
				p = p.toAbsolutePath();
				f = new File(p + "/" + "temp");
				PrintWriter out = new PrintWriter(f);
				out.close();
				f.delete();
			} catch (FileNotFoundException e) {
				System.err.println("Invalid directory given: " + outDir + "\n Saving files to project folder.");
				outDir = "";
			}
		}
	}
}

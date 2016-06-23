package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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
	public String name = "collection";
	String outDir = name+"_output";
	Date instantiatedAt = new Date();

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
	 * can be set to have the program wait until the next resource awakens.
	 */
	public abstract void filler();

	
	long it = 1; //iterations of while loop (exactly the same as total # of calls)
	
	/**
	 * Runs the data collection.
	 */
	public void run() {
		start();
		if (roadmap){
			System.out.println("Collected users after start(): ");
			for (String id : users.keySet()){
				System.out.println(users.get(id));
			}
		}
		if (verbose) System.out.println("Beginning run() while loop.");
		do {
			if (verbose || roadmap) System.out.println("current run while loop iteration: "+it);
			if (!getFollowingQ.isEmpty() && !followingSleeping()) {
				if (verbose) System.out.println("following request in run()!");
				ToFollow parent = getFollowingQ.poll();
				if (parent == null){
					if (verbose) System.out.println("null ToFollow object exempted from getFol.");
					continue;
				}
				ToUser babies = getFol(parent);
				if (followingConditions(babies))
					followAction(babies);
			}
			else if (!getUserQ.isEmpty() && !userSleeping()) {
				ToUser account = getUserQ.poll();
				if (roadmap) System.out.println("Degree of next user/users to collect: "+account.depth);
				if (account == null) continue;
				if (verbose){
					System.out.println("user request in run()!");
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
				}
				if (account.single) {
					User u = getUser(account);
					if (u == null) continue;
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
			else if (!getPostsQ.isEmpty() && !postSleeping()) {
				if (verbose) System.out.println("post request in run()!");
				getPosts(getPostsQ.poll());
			}
			if (verbose || roadmap) {
				System.out.print("Q lengths (posts, users, followers): ");
				for (int length : QueueLengths()) {
					System.out.print(length + " ");
				}
				System.out.println();
			}
			filler();
			it++;
		} while (!completed());
		System.out.println("Iterative collection completed after "+it+" calls.");
		if (!completed()) System.out.print("Completion conditions were not met, but all query queues are empty. Finishing program.");
		// ^ redundant with TwitterSample's default completed() since that just works off of queues, but useful when completed() is overriden.
		finish();
		log();
		if (verbose) System.out.println("Finish() completed. Run() completed. Log() completed.");
	}

	public void finish() {
		System.out.println("Running sample.finish(). Saving data.");
		toTSV();
	}
	
	public void log(){
		PrintWriter log = fileHandler("log.txt");
		log.println("start time: "+instantiatedAt.toString());
		Date now = new Date();
		log.println("end time: "+now.toString());
		log.println("total number of calls: "+it);
		log.close();
	}

	public int[] QueueLengths() {
		int[] sizes = { getPostsQ.size(), getUserQ.size(), getFollowingQ.size()  };
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
		long cursor = -1; //standard first cursor for twitter paging
		int depth; // depth of user we have the id of.
		User u;

		public ToFollow(User u, long cursor) {
			this(u);
			this.cursor = cursor;
		}

		public ToFollow(User u) {
			this.id = u.id;
			this.depth = u.firstDepth;
			this.u = u;
		}
	}

	public void toTSV() {
		interactionsToTSV(); // saving order is arbitrary
		usersToTSV();
		followsToTSV();
		postsToTSV();
		if (verbose) System.out.println("toTSV() completed.");
	}

	public void toTSV(String outDir) {
		this.outDir = outDir;
		toTSV();
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

//	public void loadFromTSV() {
//		String dir = System.getProperty("user.dir");
//		System.out.println("load from tsv()'s dir: " + dir);
//		loadFromTSV(dir);
//	}

//	public class Fwrap implements Comparable<Fwrap> {
//		String n;
//		File f;
//		int ordering;
//
//		public Fwrap(File f) {
//			if (f.getName().endsWith(".tsv")) {
//				this.f = f;
//				this.n = f.getName();
//				defOrd();
//			} else {
//				System.out.println("Discluding file " + f.getName());
//			}
//		}
//
//		private void defOrd() {
//			if (n.endsWith("_users.tsv")) {
//				ordering = 1;
//			} else if (n.endsWith("_posts.tsv")) { //  dependent on loading users
//				ordering = 2;
//			} else if (n.endsWith("_follows.tsv")) { // dep on users
//				ordering = 3;
//			} else if (n.endsWith("_interactions.tsv")) { //dep on posts, follows
//				ordering = 4;
//			} else {
//				ordering = 5; // all other files will ordered as 5.
//				System.out.println("Discluding file " + n);
//			}
//		}
//
//		@Override
//		public int compareTo(Fwrap o) {
//			return this.ordering - o.ordering;
//		}
//	}

	///abstract void loadFromTSV(String dir);

	public PrintWriter fileHandler(String fname) {
		outDir = name +"_output";
		checkTestOut();
		File f;
		if (outDir != null || outDir != "") {
			File outdir = new File(outDir);
			if (!outdir.exists()) {
			    System.out.println("creating directory: " + outDir);
			    outdir.mkdirs();
			}
			f = new File(outdir, fname);
		} else {
			f = new File(fname);
		}
		PrintWriter out;
		try {
			out = new PrintWriter(f);
			return out;
		} catch (FileNotFoundException e) {
			System.err.println("File could not be instantiated.");
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
				File outdir = new File(outDir);
				if (!outdir.exists()) {
				    if (verbose) System.out.println("creating directory: " + outDir);
				    outdir.mkdirs();
				}
				f = new File(outdir, "temp");
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

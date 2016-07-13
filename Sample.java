package SocialNetworkAnalysis;

import java.io.File;
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
	protected static int collected = 0; // refers to the users to whom UserConditions have been applied.
	
//	public Sample(String name) {
//		User.sample = this;
//		Interaction.sample = this;
//		Post.sample = this;
//		if (name != null && !name.equals("")){
//			this.name = name;
//			outDir = name+"_output";
//		}
//	}
//	
	public Sample(String name, String path) {
		User.sample = this;
		Interaction.sample = this;
		Post.sample = this;
		if (name != null && !name.equals("")){
			this.name = name;
			outDir = name+"_output";
		}
		else{
			System.err.println("Empty or null string passed as collection name. Calling collection \"collection\" and continuing.");
		}
		if (path != null && !path.equals("")){
			File test = new File(path);
			if (test.exists() && test.isDirectory()){
				String fs = System.getProperty("file.separator");
				outDir = path + fs + outDir;
			}
			else{
				System.err.println("Invalid path passed for output directory. Depositing output in the working directory.");
			}
		}
		else{
			System.err.println("Empty/null path passed for output directory. Depositing output in the working directory.");
		}
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
	 * Get posts from a user! should set User u's postsCollected bool to true.
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
	public void go() {
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
					if (us != null){
						for (User u : us) {
							if (userConditions(u))
								userAction(u);
						}
					}
				}
			}
			else if (!getPostsQ.isEmpty() && !postSleeping()) {
				if (verbose) System.out.println("post request in run()!");
				User u = getPostsQ.poll();
				if (!u.postsCollected)
					getPosts(u);
				else
					if (verbose) System.out.println("redundant request to getPosts ignored for user "+u.username+".");
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
		PrintWriter log = Utilities.fileHandler("log.txt");
		log.println("start time: "+instantiatedAt.toString());
		Date now = new Date();
		log.println("end time: "+now.toString());
		log.println("total number of iterations: "+it);
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
		PrintWriter w = Utilities.fileHandler(name + "_interactions.tsv");
		w.println("source\ttarget\tkind\tassociated post");
		for (int i = 0; i < allInteractions.size(); i++) {
			w.println(allInteractions.get(i));
		}
		w.close();
	}

	abstract void usersToTSV();

	abstract void followsToTSV();

	abstract void postsToTSV();

}

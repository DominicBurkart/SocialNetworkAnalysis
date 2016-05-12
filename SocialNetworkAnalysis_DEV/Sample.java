package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import SocialNetworkAnalysis.Follow;
import SocialNetworkAnalysis.Interaction;
import SocialNetworkAnalysis.Post;
import SocialNetworkAnalysis.TwitterUser;
import SocialNetworkAnalysis.User;

public abstract class Sample {
	Hashtable<String, Post> posts = new Hashtable<String, Post>();
	Hashtable<String, User> users = new Hashtable<String, User>();
	LinkedList<Follow> follows = new LinkedList<Follow>();
	String outDir;
	
	public Sample(){
		User.sample = this;
		Interaction.sample = this;
		Post.sample = this;
	}
	
	/**
	 * Determines whether there is 
	 * still more data to collect.
	 * 
	 * @return true when the collection
	 * is sufficient
	 */
	abstract boolean completed();
	
	/**
	 * Checks if we can query for 
	 * followers.
	 * 
	 * @return true if the resource is
	 * currently unaccessible.
	 */
	abstract boolean followingSleeping();
	
	/**
	 * Checks if we can query for 
	 * a user's posts.
	 * 
	 * @return true if the resource is
	 * currently unaccessible.
	 */
	abstract boolean postSleeping();
	
	/**
	 * Checks if we can query for 
	 * a user profile.
	 * 
	 * @return true if the resource is
	 * currently unaccessible.
	 */
	abstract boolean userSleeping();
	
	Queue<TwitterUser> getFollowingQ;
	Queue<TwitterUser> getPostsQ; //TODO make this more portable
	Queue<ToUser> getUserQ;
	
	/**
	 * Returns true if we should
	 * do the defined userAction to 
	 * the user.
	 */
	abstract boolean userConditions(User u);
	abstract void userAction(User u);
	
	/**
	 * Returns true if we should
	 * do the defined followAction to 
	 * the list of IDs.
	 */
	abstract boolean followingConditions(ToUser ids);
	abstract void followAction(ToUser ids);

	
	/**
	 * Seed of data collection (eg, when
	 * constructing a network of Hillary 
	 * Clinton's twitter followers, start()
	 * collects the twitter account of 
	 * Hillary Clinton).
	 */
	abstract void start();
	
	/**
	 * Get a string of IDs from a user!
	 */
	abstract String[] getFol(User u);
	
	/**
	 * Get a user from an ID!
	 */
	abstract User getUser(ToUser id);
	
	/**
	 * Get a collection of users from a list of IDs.
	 * 
	 * Assumes that all users have the same depth,
	 * which you assign to them.
	 */
	abstract User[] getUsers(ToUser ids);
	
	/**
	 * Get posts from a user!
	 */
	abstract void getPosts(User u);
	
	/**
	 * Runs the data collection.
	 */
	public void run(){
		start();
		while (!completed()){
			if (!getFollowingQ.isEmpty() && !followingSleeping()){
				User parent = getFollowingQ.poll();
				String[] ids = getFol(parent);
				ToUser babies = new ToUser(ids, parent.firstDepth+1);
				if (followingConditions(babies)) followAction(babies);
			}
			if (!getUserQ.isEmpty() && !userSleeping()){
				ToUser account = getUserQ.poll();
				if (account.single){
					User u = getUser(account);
					if (userConditions(u)) userAction(u);
				}
				else{
					User[] us = getUsers(account);
					for (User u : us){
						if (userConditions(u)) userAction(u);
					}
				}
			}
			if (!getPostsQ.isEmpty() && !postSleeping()){
				getPosts(getPostsQ.poll());
			}
		}
	}
	
	protected class ToUser{
		boolean single;
		String id;
		String[] ids;
		int depth;
		
		public ToUser(String id, int depth){
			this.id = id;
			this.depth = depth;
			single = true;
		}
		
		public ToUser(String[] ids, int depth){
			this.ids = ids;
			this.depth = depth;
			single = false;
		}
	}
	
	public void toTSV(){
		interactionsToTSV();
		usersToTSV();
		followsToTSV();
		postsToTSV();
	}
	
	public void toTSV(String outDir) {
		this.outDir = outDir;
		interactionsToTSV(); //saving order is arbitrary
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
	
	public void loadFromTSV(){
		String dir = System.getProperty("user.dir");
		System.out.println("load from csv()'s dir: "+dir);
		loadFromTSV(dir);
	}
	
	protected class Fwrap implements Comparable<Fwrap>{
		String n;
		File f;
		int ordering;
		
		public Fwrap(File f){
			if (f.getName().endsWith(".tsv")){
				this.f = f;
				this.n = f.getName();
				defOrd();
			}
			else{
				System.out.println("Discluding file "+f.getName());
			}
		}
		
		private void defOrd(){
			if (n.endsWith("_users.tsv")){
				ordering = 1;
			}
			else if (n.endsWith("_posts.tsv")){ //dependent on loading users
				ordering = 2;
			}
			else if (n.endsWith("_follows.tsv")){ // dep on users
				ordering = 3;
			}
			else if (n.endsWith("_interactions.tsv")){ //dep on posts, follows
				ordering = 4;
			}
			else{
				ordering = 5; //all other files will ordered as 5.
				System.out.println("Discluding file "+n);
			}
		}

		@Override
		public int compareTo(Fwrap o) {
			return this.ordering - o.ordering;
		}
	}
	
	abstract void loadFromTSV(String dir);

	protected PrintWriter fileHandler(String fname) {
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

	protected void checkTestOut() {
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

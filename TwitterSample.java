package SocialNetworkAnalysis;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayDeque;
import java.util.Date;

import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import twitter4j.IDs;
import twitter4j.TwitterException;

/**
 * Implements methods useful specifically for twitter samples and fleshes
 * out stuff established as important in sample. Wraps TwitterRequestHandler
 * queries and holds the boolean values of whether or not a request family is sleeping.
 *
 * @use  for use with Twitter's REST API. Not used for streaming API calls.
 * 
 * @see TwitterStreamerThread (used in the application QueerMultiStream) for a method for using streaming collection.
 * 
 * @author dominicburkart
 */
public abstract class TwitterSample extends Sample {
	protected TwitterRequestHandler t = new TwitterRequestHandler();
	static long[] open = new long[3];
	static boolean[] sleep = new boolean[3];
	public static  Hashtable<String, ArrayDeque<User>> toLinkFriends = new Hashtable<String,ArrayDeque<User>>();
	public static Hashtable<String, ArrayDeque<User>> toLinkFollowers = new Hashtable<String, ArrayDeque<User>>();
	static int[] precheckInfo = {0,0,0,0,0};

	public TwitterSample(String name, String path) {
		super(name, path);
		this.getFollowingQ = new ArrayDeque<ToFollow>();
		this.getPostsQ = new ArrayDeque<TwitterUser>();
		this.getUserQ = new ArrayDeque<ToUser>();
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
	private ArrayDeque<Integer> activeQueues(){
		ArrayDeque<Integer> active = new ArrayDeque<Integer>();
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
		ArrayDeque<Integer> active = activeQueues();
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
			System.out.println("All relevant resources are asleep. Sleeping program for "+sleep/1000+" seconds while waiting for the next resource to wake up.");
			Utilities.sleepFor(sleep);
		} catch (IllegalArgumentException e){ //thrown by Utilities.least method
			if (verbose) System.out.println("All queues are empty. Exiting filler method.");
			return;
		}
	}
	
	/**
	 * sleeps the program when no resources with non-empty queues are awake until one wakes up.
	 */
	public void userFiller(){
		if (!sleep[1]) return;
		// if we make it here then the user resource is asleep. let's figure out how long to wait.
		try{
			long sleep = open[1] - java.lang.System.currentTimeMillis();
			if (sleep > 0){
				System.out.println("User resource is asleep. Sleeping program for "+sleep/1000+" seconds while waiting for the posts to wake up.");
				Utilities.sleepFor(sleep);
			}
		} catch (IllegalArgumentException e){ //thrown by Utilities.least method
			if (verbose) System.out.println("All queues are empty. Exiting filler method.");
			return;
		}
	}
	
	/**
	 * sleeps the program when the posts resource is asleep.
	 */
	public void postsFiller(){
		if (!sleep[0]) return;
		// if we make it here then the post resource is asleep. let's figure out how long to wait.
		try{
			long sleep = open[0] - java.lang.System.currentTimeMillis();
			if (sleep > 0){
				System.out.println("Post resource is asleep. Sleeping program for "+sleep/1000+" seconds while waiting for the posts to wake up.");
				Utilities.sleepFor(sleep);
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
			if (verbose) System.out.println("ToUser object passed to getUsersFromMultiple held too many accounts! Splitting it.");
			Utilities.toUserChunker(ids); //splits the overly large toUser object and puts the chunks onto the correct queue.
			return getUsers(getUserQ.poll());
		} else
			return TwitterRequestHandler.getUsers(idsNum, ids.depth);
	}

	@Override
	public void getPosts(User u) {
		try {
			TwitterRequestHandler.getPosts(u);
			u.postsCollected = true;
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

	@Override
	public void usersToTSV() {
		PrintWriter w = Utilities.fileHandler(name + "_users.tsv"); //for master user file
		w.println("username\tid\tfirst depth\tdescription\tfavorites count\tfollowers count"
				+ "\tfriends count\ttranslator\tlanguage\tlisted count\tlocation\tname\tstatus count"
				+ "\ttimezone\tassociated url\tverified\twithheld in countries");
		Enumeration<String> keys = users.keys();
		while (keys.hasMoreElements()) { //adds a line per user to the master user file and also makes baby files for each user
			User u = users.get(keys.nextElement());
			String n = name+"_user_"+u.id+"_"; //for naming files
			w.println(u);
			if (u.getTensors().friends != null && u.getTensors().friends.size() > 0){
				PrintWriter friends = Utilities.fileHandler(n+"friends.tsv");
				for (Follow fol : u.getTensors().friends){
					friends.println(fol.target);
				}
				friends.close();
			}
			if (u.posts != null && u.posts.size() > 0){
				PrintWriter posts = Utilities.fileHandler(n+"posts.tsv");
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
		PrintWriter w = Utilities.fileHandler(name + "_follows.tsv");
		w.println("~follows~");
		for (Follow follow : follows) {
			w.println(follow);
		}
		w.close();
	}

	@Override
	public void postsToTSV() {
		PrintWriter w = Utilities.fileHandler(name + "_posts.tsv");
		w.println("id\tusername\ttime\tmessage\tauthorID\tis original\toriginal author\tretweeted from\tnotes\tcomment\ttags\tlocation\tlanguage\tpossibly sensitive link\tretweet count\tfavorite count\tsource");
		for (String k : posts.keySet()) {
			w.println(posts.get(k));
		}
		w.close();
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

	
	int checkRun = 1;
	/**
	 * collects the correct number of users (as constrained by the site architecture and the parameter "goal") that follow the user passed as the first parameter and meet these conditions:
	 * - the user is followed by and follows at least ten people.
	 * - the user has at least 30 total posts.
	 * - the user has posted within the last month.
	 * - the user's self-identified language is english.
	 * - the user follows less than 1000 other users (for computational ease).
	 */
	public void precheckTwitterFollowers(ToFollow f, int goal){
		long nextCursor = 0;
		if (roadmap || verbose) System.out.println("precheck started!");
		try {
			IDs page = TwitterRequestHandler.getTwitter4jFollowPage(f);
			getUserQ.add(TwitterRequestHandler.followPageToToUser(f, page));
			nextCursor = page.getNextCursor();
		} catch (TwitterException e) {
			System.out.println("Followers for root could not be collected. Quitting.");
			e.printStackTrace();
			System.exit(1);
		}
		while (!getUserQ.isEmpty()){
			if (!userSleeping()) {
				ToUser account = getUserQ.poll();
				if (verbose) System.out.println("collecting data in precheck.");
				if (account == null) continue;
				if (account.single) {
					User u = getUser(account);
					if (u == null) continue;
					if (!ignoreUser(u)){
//						profileCheckRecord(userCheck((TwitterUser) u));
					}
				} else {
					User[] us = getUsers(account); 
					if (us != null){
						for (User u : us) {
							profileCheckRecord(userCheck((TwitterUser) u));
						}
					}
				}
			}
			else{
				userFiller();
			}
		}
		while (!getPostsQ.isEmpty()){
			if (!postSleeping()) {
				if (verbose) System.out.println("post request in precheck()!");
				TwitterUser u = getPostsQ.poll();
				if (!u.postsCollected)
					getPosts(u);
				else
					if (verbose) System.out.println("redundant request to getPosts ignored for user "+u.username+".");
				if (precheckInfo[0] < goal) postCheckRecord(postCheck(u));
			} else{
				postsFiller();
			}
		}
		if (precheckInfo[0] < goal){
			if (nextCursor != 0){
				if (roadmap || verbose) System.out.println("Only "+precheckInfo[0]+" viable users were collected from the previous page of the inputted user's followers. Repeating process with the next page. Number of times this prechecker has been run: "+checkRun);
				checkRun++;
				f.cursor = nextCursor;
				Utilities.sleepFor((1000 * 60 * 15) / TwitterAuth.size());
				precheckTwitterFollowers(f, goal);
			}
			else{
				if (roadmap || verbose) System.out.println("The given user did not have another page of followers, so precheck finished without collecting the goal number of users. Viable users collected: "+precheckInfo[0]);
				if (roadmap || verbose) System.out.println("Continuing collection.");
			}
		}
		if (roadmap || verbose) System.out.println("precheck complete. Waiting fifteen minutes before continuing with main collection. Collected: "+precheckInfo[0]);
		Utilities.sleepFor((1000 * 60 * 15));
	}
	
	public abstract boolean ignoreUser(User u);

	/**
	 * Verifies profile is acceptable for this collection. Can be called before posts are collected.
	 * @param the user whose profile is to be checked (discluding checking of posts).
	 * @see precheckTwitterFollowers(User root, int goal)
	 * @return 0 when the user meets post constraints, 1 when the user has not set their language to English,
	 * and 4 when their following and/or friends numbers are out of range.
	 */
	public int userCheck(TwitterUser u){
		if (!u.language.equalsIgnoreCase("en")){
			if (verbose) System.out.println("Defined language for the user is not English. Language: "+u.language);
			return 1;
		}
		if (u.followersCount >= 10 && u.friendsCount >= 10 && 1000 >= u.friendsCount){
			getPostsQ.add(u);
			return 0;
		}
		if (verbose) System.out.println("User "+u.name+" excluded because their following or friend numbers did not fall into the defined ranges. Following number: "+u.followersCount+" Friend number: "+u.friendsCount);
		return 4;
	}
	
	/**
	 * Must be called after the posts of a user have been collected to work properly!
	 * @param the user whose posts are to be checked.
	 * @see precheckTwitterFollowers(User root, int goal)
	 * @return 0 when the user meets post constraints,
	 * 2 when they haven't posted in the last month, 
	 * and 3 when they don't have enough posts.
	 */
	public int postCheck(TwitterUser u){
		if (u.posts != null && u.posts.size() > 30){
			boolean lastMonth = false; //whether the user posted in the last month
			for (int i = 0; i < u.posts.size() && !lastMonth; i++){
				if ((java.lang.System.currentTimeMillis() - u.posts.get(i).getTime().getTime()) / 1000 < Utilities.MONTHSECS) lastMonth = true;
			}
			if (lastMonth){
				userAction(u);
				if (verbose || roadmap) System.out.println("User "+u.name+ " added to valid user list in Utilities.precheckTwitterFollowers().");
				return 0;
			} else{
				if (verbose && u.posts.size() > 0) System.out.println((java.lang.System.currentTimeMillis() - u.posts.get(0).getTime().getTime() / 1000) +" is less than "+ Utilities.MONTHSECS);
				if (verbose) System.out.println("User "+u.name+ "excluded because they have not posted in the last month.");
				if (verbose && u.posts.size() > 0) System.out.println("first tweet's language: "+u.posts.get(0).language);
				return 2; 
			}
		} else{
			if (verbose) System.out.println("User "+u.name+" excluded because they did not have sufficient posts. Post number: "+u.posts.size());
			return 3;
		}
	}
	
	void profileCheckRecord(int i){
		if (i != 0) precheckInfo[i]++;
	}
	
	void postCheckRecord(int i){
		precheckInfo[i]++;
	}
	
	public void log(){
		PrintWriter log = Utilities.fileHandler("log.txt");
		log.println("start time: "+instantiatedAt.toString());
		log.println("end time: "+new Date().toString());
		log.println("total number of iterations: "+it);
		log.println("total number of users checked in precheck: "+Utilities.sum(precheckInfo));
		log.println("prechecked users who met all constraints: "+precheckInfo[0]);
		log.println("prechecked users who were discluded because their language was not set to English: "+precheckInfo[1]);
		log.println("prechecked users who were discluded because they had not posted in the last month: "+precheckInfo[2]);
		log.println("prechecked users who were discluded because they did not have enough posts: "+precheckInfo[3]);
		log.println("prechecked users who were discluded because they did not have follower and friend numbers in the correct range: "+precheckInfo[4]);
		log.close();
	}
}
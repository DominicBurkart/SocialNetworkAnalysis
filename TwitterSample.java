package SocialNetworkAnalysis;

import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ArrayDeque;

import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
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

	public TwitterSample() {
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
		PrintWriter w = fileHandler(name + "_users.tsv"); //for master user file
		w.println("username\tid\tfirst depth\tdescription\tfavorites count\tfollowers count"
				+ "\tfriends count\ttranslator\tlanguage\tlisted count\tlocation\tname\tstatus count"
				+ "\ttimezone\tassociated url\tverified\twithheld in countries");
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

}

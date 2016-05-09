package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

import SocialNetworkAnalysis.Follow;
import SocialNetworkAnalysis.Post;
import SocialNetworkAnalysis.User;

public abstract class Sample {
	Hashtable<String, Post> posts = new Hashtable<String, Post>();
	Hashtable<String, User> users = new Hashtable<String, User>();
	LinkedList<Follow> follows = new LinkedList<Follow>();
	
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
	
	Queue<User> getFollowingQ;
	Queue<User> getPostsQ;
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
	abstract boolean followingConditions(String id);
	abstract void followAction(String id);
	
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
	abstract User getUser(String id, int depth);
	
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
				String[] babies = getFol(getFollowingQ.poll());
				for (String baby : babies){
					if (followingConditions(baby)) followAction(baby);
				}
			}
			if (!getUserQ.isEmpty() && !userSleeping()){
				User u = getUser(getUserQ.poll());
				if (userConditions(u)) userAction(u);
			}
			if (!getPostsQ.isEmpty() && !postSleeping()){
				getPosts(getPostsQ.poll());
			}
		}
	}
	
	private class ToUser{
		String id;
		int depth;
		
		public ToUser(String id, int depth){
			this.id = id;
			this.depth = depth;
		}
	}
}

package SocialNetworkAnalysis.Applications;

import java.util.Date;

import SocialNetworkAnalysis.*;

/**
 * For relatively small accounts– this will get unweildly when applied to roots who follow
 * more than a few hundred accounts!
 * 
 * Collects a sample of a given user's followers.
 * For each of these followers, the people who they follow are collected.
 * Samples of tweets from each of these users are collected.
 * 
 * @params first param is the root's twitter ID. The optional second input is the goal collection size.
 * 
 * @author dominicburkart
 */
public class TwitterUserFollowersFriends extends TwitterSample {
	// goal and depth must be > 0.
	static final int DEPTH = 2;
	static int goal = 300; // max collectable followers per user with this algorithm is 5k
	static String rootID;
	static TwitterUser root;
	
	public TwitterUserFollowersFriends(String name) {
		super(name, "/Volumes/Burkart/files/current_twitter_collection");
	}
	
	public static void main(String[] args) {
		System.out.println("TwitterUserFollowersFriends main is running!");
		rootID = args[0];
		if (args.length > 1){
			goal = Integer.valueOf(args[1]);
		}
		Date d = new Date();
		TwitterUserFollowersFriends h;
		if (args.length > 2){
			h = new TwitterUserFollowersFriends(args[2]+"_"+args[0]+"_"+d.toString());
		} else{
			h = new TwitterUserFollowersFriends("FollowersFriends"+args[0]+"_"+d.toString());
		}
		h.go();
		d =  new Date();
		System.out.println("Program complete. Current time: "+d.toString());
	}

	/**
	 * Tells the iterative collector to get Friends (people who the input user follows) instead of Followers (people who follow the input user)!
	 */
	@Override
	public ToUser getFol(ToFollow u) {
		return getFriends(u);
	}

	/**
	 * conditions under which we will apply userAction.
	 * 
	 * Returns true when conditions have been met.
	 * 
	 * This userConditions additionally adds every user to
	 * the getPostsQ, so we have a sample of tweets from each
	 * user.
	 */
	@Override
	public boolean userConditions(User u) {
		if (!u.fromFromPost) this.getPostsQ.add((TwitterUser) u);
		if (u != null && u.firstDepth < DEPTH && collected < goal && u != root && !u.fromPost){
			if (verbose) System.out.println("User included for userAction: "+u);
			collected++;
			return true;
		}
		else{
			try{
				if (verbose) System.out.println("User excluded from userAction: "+u);
			} catch (NullPointerException e){
				if (verbose) System.out.println("Null value passed to userConditions and exempted from userActions.");
			}
			return false;
		}
	}

	/**
	 * what to do with a user who has been collected.
	 * 
	 * In this case, tell the program to collect their followers and some posts
	 * if they pass userConditions.
	 */
	@Override
	public void userAction(User user) {
		ToFollow toFol = new ToFollow(user);
		this.getFollowingQ.add(toFol);
	}

	/**
	 * decides whether or not we apply followAction to these ids.
	 */
	@Override
	public boolean followingConditions(ToUser ids) {
		if (ids != null)
			return true;
		else
			return false;
	}

	/**
	 * followAction defines what to do with the ids we collected. In this case,
	 * we turn them into full user objects by adding them to the getUser queue.
	 */
	@Override
	public void followAction(ToUser ids) {
		if (verbose){
			System.out.print("TwitterUserFollowersFriends followAction input: ");
			if (ids.single){
				System.out.print("ToUser is single. ");
				System.out.print("id: "+ids.id);
			}
			else{
				System.out.print("ToUser holds multiple values. ");
				for (String id : ids.ids){
					System.out.print(id+" ");
				}
			}
			System.out.println("\n");
		}
		if (ids.ids.length < 100) {
			getUserQ.add(ids);
		} else { 
			Utilities.toUserChunker(ids);
		}
	}

	@Override
	public void start() {
		if (verbose) System.out.println("Starting data collection in TwitterUserFollowersFriends start()!\n");
		ToUser toRoot = new ToUser(rootID, 0);
		root = (TwitterUser) getUser(toRoot);
		ToFollow f0 = new ToFollow(root);
		ToFollow f1 = new ToFollow(root);
		getPostsQ.add(root);
		getFollowingQ.add(f0);
		precheckTwitterFollowers(f1, goal);
		if (verbose) System.out.println("start() completed.");
	}
	
	@Override
	public boolean ignoreUser(User u) {
		if (u.id.equals(root.id)) return true;
		else return false;
	}
}

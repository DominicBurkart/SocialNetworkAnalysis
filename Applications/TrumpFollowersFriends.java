package SocialNetworkAnalysis.Applications;

import java.util.Date;

import SocialNetworkAnalysis.*;

/**
 * Collects Trump's account, some of his followers, and the friends of those
 * followers. 
 * 
 * @author dominicburkart
 */
public class TrumpFollowersFriends extends TwitterSample {
	// goal and depth must be > 0.
	static final int DEPTH = 2;
	static final int GOAL = 100; // max collectable followers per user with this algorithm is 5k
	static TwitterUser root;
	
	public TrumpFollowersFriends(String name) {
		super(name, "/Volumes/Burkart/files/current_twitter_collection");
	}
	
	public static void main(String[] args) {
		System.out.println("TrumpFollowerFriends main is running!");
		Date d = new Date();
		TrumpFollowersFriends h = new TrumpFollowersFriends("trumpFollowersFriends_"+d.toString());
		h.go();
		d =  new Date();
		System.out.println("Program complete. Current time: "+d.toString());
	}

	/**
	 * Tells the iterative collector to get Friends instead of Followers!
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
		if (u != null && u.firstDepth < DEPTH && collected < GOAL && u != root && !u.fromPost){
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
			System.out.print("HillaryFollowersFriends followAction input: ");
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
		if (verbose) System.out.println("Starting data collection in TrumpFollowersFriends start()!\n");
		ToUser toRoot = new ToUser("25073877", 0);
		root = (TwitterUser) getUser(toRoot);
		ToFollow f = new ToFollow(root);
		precheckTwitterFollowers(f, GOAL);
		getPostsQ.add(root);
		if (verbose) System.out.println("start() completed.");
	}
}

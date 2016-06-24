package SocialNetworkAnalysis.Applications;

import java.util.Date;

import SocialNetworkAnalysis.*;

/**
 * Collects Clinton's account, some of her followers, and the friends of those
 * followers. 
 * 
 * @author dominicburkart
 */
public class TrumpFollowersFriends extends TwitterSample {
	static final int DEPTH = 2;
	static final int GOAL = 100; // max collectable followers per user with this algorithm is 5k
	User root;
	
	public static void main(String[] args) {
		System.out.println("TrumpFollowerFriends main is running!");
		TrumpFollowersFriends h = new TrumpFollowersFriends();
		h.name = "TrumpFollowersFriends";
		h.run();
		Date d =  new Date();
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
		this.getPostsQ.add((TwitterUser) u);
		if (u != null && u.firstDepth < DEPTH && collected < GOAL && u != root && !u.fromRepost){
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
			System.out.print("TrumpFollowersFriends followAction input: ");
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
		if (verbose) System.out.println("Starting data collection in TrumpFollowerFriends start()!\n");
		ToUser Trump = new ToUser("1339835893", 0);
		root = getUser(Trump);
		ToFollow f = new ToFollow(root);
		getUserQ.add(super.getSomeFol(f, GOAL)); //actual getSomeFollowers, where the getFollowers in this class actually gets friends.
		getPostsQ.add((TwitterUser) root);
		if (verbose) System.out.println("start() completed.");
	}
}
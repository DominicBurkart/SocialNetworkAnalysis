package SocialNetworkAnalysis.Applications;

import SocialNetworkAnalysis.*;

/**
 * Collects Clinton's account, some of her followers, and the friends of those
 * followers. 
 * 
 * @author dominicburkart
 */
public class HillaryFollowersFriends extends TwitterSample {
	static final int DEPTH = 2;
	int collected = 0;
	int goal = 5000; // max collectable followers per user with this algorithm is 5k

	
	public static void main(String[] args) {
		System.out.println("HillaryFollowerFriends main is running!");
		HillaryFollowersFriends h = new HillaryFollowersFriends();
		h.name = "hillaryFollowersFriends";
		h.run();
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
	 */
	@Override
	public boolean userConditions(User u) {
		if (u != null && u.firstDepth <= DEPTH - 1)
			return true;
		else
			return false;
	}

	/**
	 * what to do with a user who has been collected.
	 * 
	 * In this case, tell the program to collect their followers and some posts
	 * if they pass userConditions.
	 */
	@Override
	public void userAction(User user) {
		TwitterUser u = (TwitterUser) user;
		ToFollow toFol = new ToFollow(user);
		this.getFollowingQ.add(toFol);
		this.getPostsQ.add(u);
	}

	/**
	 * decides whether or not we apply followAction to these ids.
	 */
	@Override
	public boolean followingConditions(ToUser ids) {
		if (collected < goal && ids != null)
			return true;
		else
			return false;
	}

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
		if (verbose) System.out.println("Starting data collection in HillaryFollowerFriends start()!\n");
		ToUser hillary = new ToUser("1339835893", 0);
		User u = getUser(hillary);
		ToFollow f = new ToFollow(u);
		getUserQ.add(super.getFol(f)); //actual getSomeFollowers, where the getFollowers in this class actually gets friends.
		if (verbose) System.out.println("start() completed.");
	}

	@Override
	public void finish() {
		toTSV();
	}
}

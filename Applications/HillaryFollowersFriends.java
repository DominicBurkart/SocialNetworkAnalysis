package SocialNetworkAnalysis.Applications;

import SocialNetworkAnalysis.*;

import java.util.Arrays;

/**
 * Collects Clinton's account, some of her followers, and the friends of those
 * followers. 
 * 
 * @author dominicburkart
 */
public class HillaryFollowersFriends extends TwitterSample {
	static final int DEPTH = 1;
	int collected = 0;
	int goal = 5000; // max collectable followers per user with this algorithm

	
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
		if (u.firstDepth <= DEPTH - 1)
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
		if (collected < goal)
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
		} else { // splits one big ToUser object into many small enough to fit
					// into Twitter's batch querying system.
			int total = ids.ids.length;
			int chunkNum = total / 100;
			if (total % 100 > 0) {
				chunkNum++;
			}
			ToUser[] chunks = new ToUser[chunkNum];
			int last = 0;
			int i = 0;
			while (last < total) {
				int next = last + 100;
				if (next > total)
					next = total;
				String[] vals = Arrays.copyOfRange(ids.ids, last, next);
				chunks[i++] = new ToUser(vals, ids.depth);
				last = next;
			}
			for (ToUser chunk : chunks) {
				if (verbose) {
					System.out.print("chunk being added to getUserQ in HillaryFollowersFriends.followAction: ");
					for (String id : chunk.ids) {
						System.out.print(id + " ");
					}
					System.out.println("\nlength of chunk: " + chunk.ids.length+"\n");
				}
				getUserQ.add(chunk);
			}
		}
	}

	@Override
	public void start() {
		if (verbose) System.out.println("Starting data collection in HillaryFollowerFriends start()!\n");
		ToUser hillary = new ToUser("1339835893", 0);
		User u = getUser(hillary);
		ToFollow f = new ToFollow(u);
		super.getFol(f); //actual getFollowers, where the getFollowers in this class actually gets friends.
		if (verbose) System.out.println("start() completed.");
	}

	@Override
	public void finish() {
		toTSV();
	}
}

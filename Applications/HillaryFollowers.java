package SocialNetworkAnalysis.Applications;

import SocialNetworkAnalysis.*;

import java.util.Arrays;

public class HillaryFollowers extends TwitterSample {
	static final int DEPTH = 1;
	int collected = 0;
	int goal = 5000; // max collectable followers per user with this algorithm

	public static void main(String[] args) {
		HillaryFollowers h = new HillaryFollowers();
		h.name = "hillaryFollowers";
		h.run();
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
				chunks[i] = new ToUser(vals, ids.depth);
				last = next;
				i++;
			}
			for (ToUser chunk : chunks) {
				if (verbose) {
					for (String id : chunk.ids) {
						System.out.print(id + " ");
					}
					System.out.println("\nlength: " + chunk.ids.length);
				}
				getUserQ.add(chunk);
			}
		}
	}

	@Override
	public void start() {
		System.out.println("Starting data collection!");
		ToUser hillary = new ToUser("1339835893", 0);
		getUserQ.add(hillary);
	}

	@Override
	public void finish() {
		toTSV();
	}

}

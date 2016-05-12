package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Arrays;

import SocialNetworkAnalysis.TwitterUser;
import SocialNetworkAnalysis.User;

public class HillaryFollowers extends TwitterSample {
	static final int DEPTH = 1;
	int collected = 0;
	int goal = 50000;
	
	public static void main(String[] args){
		HillaryFollowers h = new HillaryFollowers();
		h.run();
	}

	/**
	 * conditions under which we will apply userAction.
	 * 
	 * Returns true when conditions have been met.
	 */
	@Override
	boolean userConditions(User u) {
		if (u.firstDepth < DEPTH-1) return true;
		else return false;
	}

	/**
	 * what to do with a user who has been collected.
	 * 
	 * In this case, tell the program to collect their
	 * followers if they pass userConditions.
	 */
	@Override
	void userAction(User u) {
		this.getFollowingQ.add((TwitterUser) u);
	}

	/**
	 * decides whether or not we apply followAction
	 * to these ids.
	 */
	@Override
	boolean followingConditions(ToUser ids) {
		if (collected < goal) return true;
		else return false;
	}
	
	@Override
	void followAction(ToUser ids) {
		if (ids.ids.length < 100){
			getUserQ.add(ids);
		}
		else{
			int total = ids.ids.length;
			int chunkNum = total / 100;
			if (total % 100 > 0){
				chunkNum++;
			}
			ToUser[] chunks = new ToUser[chunkNum];
			int last = 0;
			int i = 0;
			while (last < total){
				int next = last +100;
				if (next > total) next = total;
				String[] vals = Arrays.copyOfRange(ids.ids, last, next);
				chunks[i] = new ToUser(vals, ids.depth);
				last = next;
				i++;
			}
			for (ToUser chunk : chunks){
				getUserQ.add(chunk);
			}
		}
	}

	@Override
	void start() {
		System.out.println("Starting data collection!");
		ToUser hillary = new ToUser("1339835893", 0);
		getUserQ.add(hillary);
	}

}

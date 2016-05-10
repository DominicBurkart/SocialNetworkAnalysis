package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

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
		this.getFollowingQ.add(u);
	}

	/**
	 * decides whether or not we apply followAction
	 * to these ids.
	 */
	@Override
	boolean followingConditions(String[] ids) {
		if (collected < goal) return true;
		else return false;
	}

	/**
	 * what to do with some ids that have been collected.
	 */
	@Override
	void followAction(String[] ids) {
		
	}

	@Override
	void start() {
		//TODO get hillary clinton's twitter
	}

}

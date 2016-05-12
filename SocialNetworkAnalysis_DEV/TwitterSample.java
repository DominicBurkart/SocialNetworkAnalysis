package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Arrays;
import java.util.LinkedList;

import SocialNetworkAnalysis.BadUserException;
import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import SocialNetworkAnalysis.User;
import SocialNetworkAnalysis.TwitterUser;
import twitter4j.TwitterException;

public abstract class TwitterSample extends Sample {
	TwitterRequestHandler t = new TwitterRequestHandler();
	long[] open = TwitterAuth.open;
	boolean fsleep;
	boolean usleep;
	boolean ssleep;
	boolean[] sleep = {ssleep, usleep, fsleep};
	
	public TwitterSample(){
		this.getFollowingQ = new LinkedList<TwitterUser>();
		this.getPostsQ = new LinkedList<TwitterUser>();
		this.getUserQ = new LinkedList<ToUser>();
	}

	@Override
	boolean completed() {
		if (this.getFollowingQ.size() == 0 &&
			this.getPostsQ.size() == 0 &&
			this.getUserQ.size() == 0)
			return true;
		return false;
	}
	
	static String[] fams = {"Status", "User", "Following"};
	private boolean sleeping(int i){

		if (!sleep[i]) return false;
		else{
			long now = java.lang.System.currentTimeMillis();
			if (now > open[i]){
				sleep[i] = false;
				System.out.println(fams[i]+" family is resuming queries.");
				return false;
			}
			else{
				return true;
			}
		}
	}

	@Override
	boolean followingSleeping() {
		return sleeping(2);
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
	String[] getFol(User u) {
		try {
			return TwitterRequestHandler.getSomeFollowerIds(u);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	@Override
	User getUser(ToUser id) {
		try {
			User u = TwitterRequestHandler.getUser(Long.parseLong(id.id), id.depth);
			return u;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (BadUserException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	@Override
	User[] getUsers(ToUser ids){
		long[] idsNum = new long[ids.ids.length];
		int i = 0;
		for (String id : ids.ids){
			idsNum[i] = Long.parseLong(id);
			i++;
		}
		//algorithm: if ids >= 100, just break the request into multiple new requests + push them to the user queue!
		if (ids.ids.length > 100){
			System.err.println("Bad list of IDs to getUsers. Max depth is 100 and this input had a depth of "+ids.ids.length);
			System.err.println("Attempting to return the first 100 users.");
			return TwitterRequestHandler.getUsers(Arrays.copyOf(idsNum, 100), ids.depth);
		}
		else return TwitterRequestHandler.getUsers(idsNum, ids.depth);
	}

	@Override
	void getPosts(User u) {
		try {
			TwitterRequestHandler.getPosts(u);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.err.println("continuing data collection.");
		}
	}
	
	
	class Listener implements Ratelimit_Reached_Listener{
		@Override
		public void reached(int i) {
			switch (i){
			case 0: ssleep = true; break;
			case 1: usleep = true; break;
			case 2: fsleep = true; break;
			default: System.err.println("Weird value passed to reached() method in TwitterSample.Listener");
			}
		}
	}

}

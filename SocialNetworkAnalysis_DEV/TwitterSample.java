package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Date;

import SocialNetworkAnalysis.BadUserException;
import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import SocialNetworkAnalysis.User;
import twitter4j.TwitterException;

public class TwitterSample extends Sample {
	TwitterRequestHandler t = new TwitterRequestHandler();
	long[] open = TwitterAuth.open;
	boolean fsleep;
	boolean usleep;
	boolean ssleep;
	boolean[] sleep = {ssleep, usleep, fsleep};
	
	public static void main(String[] args){
		TwitterSample s = new TwitterSample();
		s.run();
	}

	@Override
	boolean completed() {
		if (this.getFollowingQ.size() == 0 &&
			this.getPostsQ.size() == 0 &&
			this.getUserQ.size() == 0)
			return true;
		return false;
	}
	
	private boolean sleeping(int i){
		if (!sleep[i]) return false;
		else{
			long now = java.lang.System.currentTimeMillis();
			if (now > open[i]){
				sleep[i] = false;
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
	boolean userConditions(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void userAction(User u) {
		// TODO Auto-generated method stub

	}

	@Override
	boolean followingConditions(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void followAction(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void start() {
		// TODO Auto-generated method stub

	}

	@Override
	String[] getFol(User u) {
		
	}

	@Override
	User getUser(String id, int depth) {
		User u;
		try {
			u = TwitterRequestHandler.getUser(Long.parseLong(id), depth);
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
	void getPosts(User u) {
		// TODO Auto-generated method stub
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

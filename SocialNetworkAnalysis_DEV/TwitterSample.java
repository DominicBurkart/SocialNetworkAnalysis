package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.Date;

import SocialNetworkAnalysis.Ratelimit_Reached_Listener;
import SocialNetworkAnalysis.User;

public class TwitterSample extends Sample {

	@Override
	boolean completed() {
		if (this.getFollowingQ.size() == 0 &&
			this.getPostsQ.size() == 0 &&
			this.getUserQ.size() == 0)
			return true;
		return false;
	}

	@Override
	boolean followingSleeping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean postSleeping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean userSleeping() {
		// TODO Auto-generated method stub
		return false;
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
	boolean followingConditions(String[] ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void followAction(String[] ids) {
		// TODO Auto-generated method stub

	}

	@Override
	void start() {
		// TODO Auto-generated method stub

	}

	@Override
	String[] getFol(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	User getUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void getPosts(User u) {
		// TODO Auto-generated method stub

	}
	
	class Fol_listener implements Ratelimit_Reached_Listener{
		Date last;

		@Override
		public void reached() {
			// TODO Auto-generated method stub
			
		}
	}
	
	class Post_listener{
		
	}
	
	class User_sleeping{
		
	}

}

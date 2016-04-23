package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterRequestHandler{
	private static TwitterAuth authorization = new TwitterAuth();
	private static Twitter twitter = authorization.getInstance();
	
	static Twitter getTwitter(){
		checkTwitter();
		return twitter;
	}
	
	static Twitter checkTwitter(){
		try {
			Map<String ,RateLimitStatus> rateLimit = twitter.getRateLimitStatus();
			for (String timeStatus : rateLimit.keySet()) {
				RateLimitStatus timeLeft = rateLimit.get(timeStatus);
				if (timeLeft != null && timeLeft.getRemaining() == 0) {
					twitter = authorization.getInstance();
					checkTwitter(twitter, 0);
					return twitter;
        	    }
			}
		} catch (TwitterException e) {
			System.err.println("Error checking rateLimitStatus.");
		}
		return twitter;
	}
	
	//Recursively iterates through authorization tokens
	private static Twitter checkTwitter(Twitter t, int i){
		if (i >= authorization.size()){
			System.err.println("Rate limit reached.");
			try {
				Thread.sleep(15 * 60 * 1000);
				i--;
			} catch (InterruptedException e) {
				System.err.println("Sleep error occured in checkTwitter");
			}
		}
		try {
			//TODO figure out something more sensical than this!!!!!
			Map<String ,RateLimitStatus> rateLimit = t.getRateLimitStatus();
			for (String timeStatus : rateLimit.keySet()) {
				RateLimitStatus timeLeft = rateLimit.get(timeStatus);
				if (timeLeft != null && timeLeft.getRemaining() == 0) {
					t = authorization.getInstance();
					checkTwitter(t, i+1);
					return t;
        	    }
			}
		} catch (TwitterException e) {
			System.err.println("Error checking rateLimitStatus.");
		}
		return t;
	}
    
	static ArrayList<User> getFollowers(User u) throws BadIDException{
		ArrayList<User> out = new ArrayList<User>();
		ArrayList<long[]> pages = new ArrayList<long[]>();
		try {
			long cursor = -1;
			while (cursor != 0){
				IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), cursor);
				pages.add(IDvals.getIDs());
				cursor = IDvals.getNextCursor();
			}
			for (long[] p: pages){
				for(long l : p){
					TwitterUser t = getUser(l, u.firstDepth + 1);
					out.add(t);
				}
			}
		} catch (NumberFormatException e) {
			throw new BadIDException("Bad id given to getFollowers: "+u.id);
		} catch (TwitterException e) {
			System.err.println("problem with finding twitter user: "+u.id);
		} catch (BadUserException e) {
			System.err.println("Unusual error with twitter user: "+u.id);
		}
		return out;
	}

	static TwitterUser getUser(long id, int depth) throws BadUserException, TwitterException{
		try {
			TwitterUser t = new TwitterUser(Long.toString(id), depth);
			twitter4j.User tUser = getTwitter().showUser(id);
			t.description = tUser.getDescription();
			t.username = tUser.getDescription();
			return t;
		} catch (RedundantEntryException e) {
			return (TwitterUser) e.user;
		}
	}
	
	static void getPosts(User u) throws TwitterException, RedundantEntryException{
		Paging paging = new Paging(1, 100);
		List<Status> statuses = getTwitter().getUserTimeline(u.username,paging);
		for (Status s : statuses){
			Post p = new TwitterStatus();
			p.id = Long.toString(s.getId());
			p.notes = s.getFavoriteCount();
			p.message = s.getText();
			p.time = s.getCreatedAt();
			p.author = u;
			p.original = s.isRetweet();
			p.location.latitude = s.getGeoLocation().getLatitude();
			p.location.longitude = s.getGeoLocation().getLongitude();
			p.location.name = s.getPlace().getFullName();
			p.location.locationType = s.getPlace().getPlaceType();
			u.addPost(p);
		}
	}
	
	static void getReposted(Post p) throws NumberFormatException, TwitterException, BadUserException{
		long cursor = -1;
		ArrayList<Long> ids = new ArrayList<Long>();
		while (cursor != 0){
			IDs pages = twitter.getRetweeterIds(Long.valueOf(p.id), -1);
			for(long id : pages.getIDs()){
				
				ids.add(id);
				
				try{
					getUser(id, p.author.firstDepth +1);
				}
				catch (RedundantEntryException e){}; //not important here
				
				User reposter = Post.sample.users.get(Long.toString(id));
				Repost r = new Repost(p, reposter, p.author); //action originates in reposter and goes to poster
				reposter.tensors.add(r);
			}
		}
	}
}

package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterRequestHandler {
	private static TwitterAuth authorization = new TwitterAuth();
	static TwitterFactory factory = authorization.getFactory();
    
	static ArrayList<User> getFollowers(TwitterUser u) throws BadIDException{
		Twitter twitter = factory.getInstance();
		ArrayList<User> out = new ArrayList<User>();
		ArrayList<long[]> pages = new ArrayList<long[]>();
		try {
			long cursor = -1;
			while (cursor != 0){
				IDs IDvals = twitter.getFollowersIDs(Long.valueOf(u.id), cursor);
				pages.add(IDvals.getIDs());
				cursor = IDvals.getNextCursor();
			}
			for (int i = 0; i < pages.size(); i++){
				long[] p = pages.get(i);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	static TwitterUser getUser(long id, int depth) throws BadUserException{
		try {
			TwitterUser t = new TwitterUser(Long.toString(id), depth);
			//TODO collect relevant data on user
			return t;
		} catch (RedundantEntryException e) {
			System.err.println("Redundant instatiation attempted for user with id "+id);
		}
		throw new BadUserException("User was not correctly substantiated: "+String.valueOf(id));
	}
	
	static void getPosts(User u) throws TwitterException, RedundantEntryException{
		Twitter twitter = factory.getInstance();
		Paging paging = new Paging(1, 100);
		List<Status> statuses = twitter.getUserTimeline(u.username,paging);
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
	
	static void getLikes(Post p){
		//TODO
	}
	
	static void getRetweeted(Post p){
		Twitter twitter = factory.getInstance();
		
		twitter.getRetweeterIds(Long.valueOf(p.id), );
	}
	
//	static void getComments(Post p){         necessary?
//		
//	}
	
}

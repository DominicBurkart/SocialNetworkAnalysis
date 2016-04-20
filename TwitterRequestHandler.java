package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterRequestHandler{
	private static TwitterAuth authorization = new TwitterAuth();
	static TwitterFactory factory = authorization.getFactory();
	static Twitter twitter = factory.getInstance();
	
	static void rateLimited(){
		
	}
    
	static ArrayList<User> getFollowers(User u) throws BadIDException{
		ArrayList<User> out = new ArrayList<User>();
		ArrayList<long[]> pages = new ArrayList<long[]>();
		try {
			long cursor = -1;
			while (cursor != 0){
				try{
					IDs IDvals = twitter.getFollowersIDs(Long.valueOf(u.id), cursor);
					pages.add(IDvals.getIDs());
					cursor = IDvals.getNextCursor();
				}
				catch (TwitterException e){
					rateLimited();
				}
			}
			for (long[] p: pages){
				for(long l : p){
					try{
						TwitterUser t = getUser(l, u.firstDepth + 1);
						out.add(t);
					}
					catch (TwitterException e){
						rateLimited();
					}
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
			//TODO actually input user info
			return t;
		} catch (RedundantEntryException e) {
			System.err.println("Redundant instatiation attempted for user with id "+id);
		}
		throw new BadUserException("User was not correctly substantiated: "+String.valueOf(id));
	}
	
	static void getPosts(User u) throws TwitterException, RedundantEntryException{
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
	
//	static void getComments(Post p){         necessary?
//		
//	}
	
}

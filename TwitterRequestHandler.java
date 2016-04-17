package SocialNetworkAnalysis;

import java.util.ArrayList;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterRequestHandler {
	static TwitterFactory factory = new TwitterFactory();
    
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
		String username = ""; //TODO get username
		try {
			TwitterUser t = new TwitterUser(username, Long.toString(id), depth);
			//TODO collect relevant data on user
			return t;
		} catch (RedundantEntryException e) {
			System.err.println("Redundant instatiation attempted for user "+username+" with id "+id);
		}
		throw new BadUserException("User was not correctly substantiated: "+String.valueOf(id));
	}
	
	static void getPosts(User u){
		
	}
}

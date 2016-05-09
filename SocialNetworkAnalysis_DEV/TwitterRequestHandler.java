package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Is called to mediate requests between the program built on this package and the TwitterAuth object beneath it.
 * 
 * @author dominicburkart
 */
public class TwitterRequestHandler {
	private static TwitterAuth authorized = new TwitterAuth();

	static Twitter getTwitter() {
		return authorized.getTwitter();
	}

	static ArrayList<User> getFollowers(User u) throws BadIDException, TwitterException {
		ArrayList<User> out = new ArrayList<User>();
		ArrayList<long[]> pages = new ArrayList<long[]>();
		try {
			long cursor = -1;
			while (cursor != 0) {
				IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), cursor);
				pages.add(IDvals.getIDs());
				cursor = IDvals.getNextCursor();
			}
			for (long[] p : pages) {
				for (long l : p) {
					try{
						TwitterUser t = getUser(l, u.firstDepth + 1);
						out.add(t);
					} catch(TwitterException e){
						if (e.getErrorCode() == 50){
							System.err.println("User could not be found:"+l+"\ncontinuing collection.");
						}
						else if (e.getErrorCode() == 63) {
							System.err.println("User "+l+" has been suspended and ignored.");
							continue;
						}
						else throw e;
					}
				}
			}
		} catch (NumberFormatException e) {
			throw new BadIDException("Bad id given to getFollowers: " + u.id);
		} catch (BadUserException e) {
			System.err.println("Unusual error with twitter user: " + u.id);
		}
		return out;
	}
	
	static ArrayList<User> getSomeFollowers(User u) throws BadIDException, TwitterException {
		ArrayList<User> out = new ArrayList<User>();
		try {
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), -1);
			for (long l : IDvals.getIDs()) {
				try{
					TwitterUser t = getUser(l, u.firstDepth + 1);
					out.add(t);
				} catch(TwitterException e){
					if (e.getErrorCode() == 50){
						System.err.println("User could not be found:"+l+"\ncontinuing collection.");
					}
					else if (e.getErrorCode() == 63) {
						System.err.println("User "+l+" has been suspended and ignored.");
						continue;
					}
					else throw e;
				}
			}
		} catch (BadUserException e) {
			System.err.println("Unusual error with twitter user: " + u.id);
		} catch (TwitterException e){
			if (e.getErrorCode() != -1) throw e;
			//otherwise this is just a protected user.
		}
		return out;
	}
	
	private static int Collected(LinkedList<long[]> l){
		int total = 0;
		for (long[] p : l){
			total += p.length;
		}
		return total;
	}
	
	/**
	 * 
	 * Returns at most x followers for each call. Less could be called if the user doesn't follow
	 * a lot of people.
	 * 
	 * @param u
	 * @param num
	 * @return
	 * @throws BadIDException
	 * @throws TwitterException
	 */
	static ArrayList<User> getxFollowers(User u, int num) throws BadIDException, TwitterException {
		ArrayList<User> out = new ArrayList<User>();
		try {
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), -1);
			long[] ids = IDvals.getIDs();
			if (ids.length < num){ //either the user doesn't follow many people or we didn't get enough from twitter
				if (IDvals.getNextCursor() != 0 ){ //if we can get more from twitter, do
					LinkedList<long[]> pages = new LinkedList<long[]>();
					pages.add(ids);
					while (IDvals.getNextCursor() != 0 && Collected(pages) < num){
						IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), IDvals.getNextCursor());
						ids = IDvals.getIDs();
						pages.add(ids);
					}
					for (long[] page : pages){ // go through and add more users until we're done
						for(long id : page){
							if (out.size() < num){
								try{
									TwitterUser t = getUser(id, u.firstDepth + 1);
									out.add(t);
								} catch(TwitterException e){
									if (e.getErrorCode() == 50){
										System.err.println("User could not be found:"+id+"\ncontinuing collection.");
									}
									else if (e.getErrorCode() == 63) {
										System.err.println("User "+id+" has been suspended and ignored.");
										continue;
									}
									else throw e;
								}
							}
							else{ //we're done!
								return out;
							}
						}
					}
				}
				else{ // the user doesn't follow many people. just return who they do follow.
					for(long id : ids){			
						try{
							TwitterUser t = getUser(id, u.firstDepth + 1);
							out.add(t);
						} catch(TwitterException e){
							if (e.getErrorCode() == 50){
								System.err.println("User could not be found:"+id+"\ncontinuing collection.");
							}
							else if (e.getErrorCode() == 63) {
								System.err.println("User "+id+" has been suspended and ignored.");
								continue;
							}
							else throw e;
						}
					}
				}
			}
			else{ //we collected enough followers with one getFollowers call! great!
				for (int i = 0; i < num; i++) {
					long l = ids[i];
					try{
						TwitterUser t = getUser(l, u.firstDepth + 1);
						out.add(t);
					} catch(TwitterException e){
						if (e.getErrorCode() == 50){
							System.err.println("User could not be found:"+l+"\ncontinuing collection.");
						}
						else throw e;
					}
				}
			}
		} catch (BadUserException e) {
			System.err.println("Unusual error with twitter user: " + u.id);
		} catch (TwitterException e){
			if (e.getErrorCode() != -1) throw e;
			System.err.println("Unusual error with twitter user: " + u.id);
			if (u.username != null){
				System.err.println("username of weird error account: "+ u.username);
			}
			else{
				System.err.println("username of weird error account was null.");
			}
		}
		return out;
	}

	static TwitterUser getUser(long id, int depth) throws BadUserException, TwitterException {
		try {
			TwitterUser t = new TwitterUser(Long.toString(id), depth);
			twitter4j.User tUser = getTwitter().showUser(id);
			t.description = tUser.getDescription();
			t.username = tUser.getScreenName();
			return t;
		} catch (RedundantEntryException e) {
			return (TwitterUser) e.user;
		}
	}

	static void getPosts(User u) throws TwitterException {
		Paging paging = new Paging(1, 100);
		List<Status> statuses = getTwitter().getUserTimeline(u.username, paging);
		for (Status s : statuses) {
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
			try {
				u.addPost(p);
			} catch (RedundantEntryException e) {}
		}
	}

	static void getReposted(Post p) throws NumberFormatException, TwitterException, BadUserException {
		long cursor = -1;
		ArrayList<Long> ids = new ArrayList<Long>();
		while (cursor != 0) {
			IDs pages = getTwitter().getRetweeterIds(Long.valueOf(p.id), -1);
			for (long id : pages.getIDs()) {

				ids.add(id);

				try {
					getUser(id, p.author.firstDepth + 1);
				} catch (RedundantEntryException e) {} // not important here
				catch (TwitterException e){
					if (e.getErrorCode() == 50) continue;
					else if (e.getErrorCode() == 63) {
						System.err.println("User "+id+" has been suspended and ignored.");
						continue;
					}
					else throw e;
				}

				User reposter = Post.sample.users.get(Long.toString(id));
				Repost r = new Repost(p, reposter, p.author);
				//^ Action orginates in reposter and goes to poster
				reposter.tensors.add(r);
			}
		}
	}
}

package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import SocialNetworkAnalysis.Sample.ToFollow;
import SocialNetworkAnalysis.Sample.ToUser;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Is called to mediate requests between the program built on this package and
 * the TwitterAuth object beneath it.
 * 
 * @author dominicburkart
 */
public class TwitterRequestHandler extends SNA_Root {
	private static TwitterAuth authorized = new TwitterAuth();

	static Twitter getTwitter() {
		return authorized.getTwitter();
	}
	
	
	static TwitterStatus[] search(String terms) throws TwitterException {
		Query q = new Query(terms);
		try{
			List<Status> l = getTwitter().search(q).getTweets();
			TwitterStatus[] out = new TwitterStatus[l.size()];
			int i = 0;
			for (Status s : l) {
				out[i] = new TwitterStatus(s);
			}
			return out;
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
	}

	static ToUser getFriends(ToFollow toFriend) throws BadIDException, TwitterException {
		ToUser out = null;
		try {
			// get the user ids!
			IDs IDvals = getTwitter().getFriendsIDs(Long.valueOf(toFriend.id), toFriend.cursor);
			long[] ids = IDvals.getIDs();
			String[] sIds = new String[ids.length];
			int i = 0;
			User friender = User.sample.users.get(toFriend.id);
			for (long id : ids){
				sIds[i] = Long.toString(id);
				LinkedList<User> uL = TwitterSample.toLinkFriends.get(sIds[i]);
				if (uL == null){
					uL = new LinkedList<User>();
					uL.add(friender);
					TwitterSample.toLinkFriends.put(sIds[i], uL);
				}
				else{
					uL.add(friender);
				}
				i++;
			}
			out = User.sample.new ToUser(sIds, toFriend.depth + 1);

			// we only collected the first batch of followers for this user.
			// If there are more, add the remaining back to the
			// getFollowingQueue.
			if (IDvals.getNextCursor() != 0) {
				toFriend.cursor = IDvals.getNextCursor();
				User.sample.getFollowingQ.add(toFriend);
			}
		} catch (NumberFormatException e) {
			try {
				throw new BadIDException("Bad id given to getFollowers: " + toFriend.id);
			} catch (NullPointerException f) {
				throw new BadIDException("Null value given to getFollowers as an id");
			}
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
		return out;
	}

	static ToUser getSomeFriends(ToFollow toFriend) throws BadIDException, TwitterException {
		ToUser out = null;
		try {
			IDs IDvals = getTwitter().getFriendsIDs(Long.valueOf(toFriend.id), toFriend.cursor);
			long[] ids = IDvals.getIDs();
			String[] sIds = new String[ids.length];
			User friender = User.sample.users.get(toFriend.id);
			int i = 0;
			for (long id : ids){
				sIds[i] = Long.toString(id);
				LinkedList<User> uL = TwitterSample.toLinkFriends.get(sIds[i]);
				if (uL == null){
					uL = new LinkedList<User>();
					uL.add(friender);
					TwitterSample.toLinkFriends.put(sIds[i], uL);
				}
				else{
					uL.add(friender);
				}
				i++;
			}
			out = User.sample.new ToUser(sIds, toFriend.depth + 1);
			return out;
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
	}

	static ToUser getFollowers(ToFollow toFol) throws BadIDException, TwitterException {
		if (verbose) System.out.println("getFollowers called.");
		ToUser out = null;
		// get the user ids!
		try{
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(toFol.id), toFol.cursor); 
			long[] ids = IDvals.getIDs();
			if (verbose){
				System.out.print("incoming id list in TwitterRequestHandler.getFollowers: ");
				for (long id : ids){
					System.out.print(id+" ");
				}
				System.out.println();
			}
			String[] sIds = new String[ids.length];
			int i = 0;
			for (long vId : ids){ //builds sIds, fills toLinkFollowers
				String id = sIds[i++] = Long.toString(vId);
				if (TwitterSample.toLinkFollowers.containsKey(id)){
					TwitterSample.toLinkFollowers.get(id).add(toFol.u);
				} else{
					User target = toFol.u;
					LinkedList<User> uL = new LinkedList<User>();
					uL.add(target);
					TwitterSample.toLinkFollowers.put(id, uL);
				}
			}
			out = User.sample.new ToUser(sIds, toFol.depth + 1);

			// we only collected the first batch of followers for this user.
			// If there are more, add the remaining back to the
			// getFollowingQueue.
			if (IDvals.getNextCursor() != 0) {
				toFol.cursor = IDvals.getNextCursor();
				User.sample.getFollowingQ.add(toFol);
			}
			return out;
		} catch (NumberFormatException e) {
			try {
				throw new BadIDException("Bad id given to getFollowers: " + toFol.id);
			} catch (NullPointerException f) {
				throw new BadIDException("Null value given to getFollowers as an id");
			}
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
	}

	static ToUser getSomeFollowers(ToFollow toFol) throws BadIDException, TwitterException {
		ToUser out = null;
		try {
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(toFol.id), toFol.cursor);
			long[] ids = IDvals.getIDs();
			String[] sIds = new String[ids.length];
			int i = 0;
			for (long vId : ids){
				String id = sIds[i++] = Long.toString(vId);
				if (TwitterSample.toLinkFollowers.containsKey(id)){
					TwitterSample.toLinkFollowers.get(id).add(toFol.u);
				} else{
					User target = toFol.u;
					LinkedList<User> uL = new LinkedList<User>();
					uL.add(target);
					TwitterSample.toLinkFollowers.put(id, uL);
				}
			}
			out = User.sample.new ToUser(sIds, toFol.depth + 1);
			return out;
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
	}
	
	/**
	 * returns the least of these possible values:
	 *  1. x followers of a user (where x is passed to the function),
	 *  2. 5000 followers for a user, or
	 *  3. all of the followers of a given user.
	 *  
	 * @param f the toFollow element to find followers from.
	 * @param x the number of followers to try to collect.
	 * @return the toUser elements collected in a linked list.
	 * @throws TwitterException
	 * @throws BadUserException
	 */
	static ToUser getxFollowers(ToFollow f, int x) throws TwitterException{
		try{
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(f.id), -1);
			int[] limits = {IDvals.getIDs().length , x};
			int max = Utilities.least(limits);
			String[] ids = new String[max];
			for (int i = 0; i < max; i++){
				long l = IDvals.getIDs()[i];
				String id = ids[i] = Long.toString(l);
				if (TwitterSample.toLinkFollowers.containsKey(id)){
					TwitterSample.toLinkFollowers.get(id).add(f.u);
				} else{
					User target = f.u;
					LinkedList<User> uL = new LinkedList<User>();
					uL.add(target);
					TwitterSample.toLinkFollowers.put(id, uL);
				}
			}
			ToUser t = User.sample.new ToUser(ids, f.depth+1);
			return t;
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return null;
		}
	}

	/**
	 * @see getUsers
	 * 
	 * @param id
	 * @param depth
	 * @return
	 * @throws BadUserException
	 * @throws TwitterException
	 */
	static TwitterUser getUser(long id, int depth) throws BadUserException {
		try {
			TwitterUser t = new TwitterUser(Long.toString(id), depth);
			twitter4j.User tUser = getTwitter().showUser(id);
			t.description = tUser.getDescription();
			t.username = tUser.getScreenName();
			return t;
		} catch (RedundantEntryException e) {
			return (TwitterUser) e.user;
		} catch (TwitterException e){
			if (verbose) e.printStackTrace();
			try{
				Utilities.handleTwitterException(e);
			} catch(TwitterException f){
				f.printStackTrace();
				System.err.println("Unhandled exception. quitting");
				System.exit(0);
			}
			return null;
		}
	}

	static TwitterUser[] getUsers(long[] ids, int depth) {
		ResponseList<twitter4j.User> accounts;
		try {
			accounts = getTwitter().lookupUsers(ids);
			TwitterUser[] out = new TwitterUser[accounts.size()];
			int i = 0;
			for (twitter4j.User t : accounts) {
				try {
					TwitterUser u = new TwitterUser(Long.toString(t.getId()), depth);
					u.description = t.getDescription();
					u.username = t.getScreenName();
					out[i] = u;
				} catch (RedundantEntryException e) {
					out[i] = (TwitterUser) e.user;
				} 
				i++;
			}
			return out;
		} catch (TwitterException e){
			try{
				Utilities.handleTwitterException(e);
			} catch(TwitterException f){
				f.printStackTrace();
				System.err.println("Unhandled exception. quitting");
				System.exit(0);
			}
			return null;
		}
	}

	static void getPosts(User u) throws TwitterException {
		if (u == null) return;
		// TODO fill this out or use twitter4j's user implementation bc there's
		// a lot more to work with!
		Paging paging = new Paging(1, 200); //200 is the max page size for this twitter resource as of june 2016
		try{
			List<Status> statuses = getTwitter().getUserTimeline(u.username, paging);
			for (Status s : statuses) {
				// TODO save raw statuses via a new TwitterSample method
				if (verbose) System.out.println("status collected in TwitterRequestHandler.getPosts: "+s.toString());
				Post p = new TwitterStatus(s);
				try {
					u.addPost(p);
				} catch (RedundantEntryException e) {}
			}
		} catch (TwitterException e){
			Utilities.handleTwitterException(e);
			return;
		}
	}

	static void getReposted(Post p) throws NumberFormatException, TwitterException, BadUserException {
		long cursor = -1;
		ArrayList<Long> ids = new ArrayList<Long>();
		while (cursor != 0) {
			IDs pages = getTwitter().getRetweeterIds(Long.valueOf(p.getId()), -1);
			for (long id : pages.getIDs()) {

				ids.add(id);

				try {
					getUser(id, p.getAuthor().firstDepth + 1);
				} catch (RedundantEntryException e) {
				} // not important here

				User reposter = Post.getSample().users.get(Long.toString(id));
				Repost r = new Repost(p, reposter, p.getAuthor());
				// ^ Action orginates in reposter and goes to poster
				reposter.getTensors().add(r);
			}
		}
	}
}

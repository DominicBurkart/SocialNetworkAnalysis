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
		List<Status> l = getTwitter().search(q).getTweets();
		TwitterStatus[] out = new TwitterStatus[l.size()];
		int i = 0;
		for (Status s : l) {
			out[i] = new TwitterStatus(s);
		}
		return out;
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
			if (e.getErrorCode() == 401){
				return null;
			}
		}
		return out;
	}

	static ToUser getSomeFriends(ToFollow toFriend) throws BadIDException, TwitterException {
		ToUser out = null;
		try {
			IDs IDvals = getTwitter().getFriendsIDs(Long.valueOf(toFriend.id), toFriend.cursor);
			long[] ids = IDvals.getIDs();
			String[] sIds = new String[ids.length];
			out = User.sample.new ToUser(sIds, toFriend.depth + 1);
		} catch (TwitterException e) {
			if (e.getErrorCode() != -1)
				throw e;
			// otherwise this is just a protected user or we're gettting a
			// similar permissions problem.
		}
		return out;
	}

	static ToUser getFollowers(ToFollow toFol) throws BadIDException, TwitterException {
		System.out.println("getFollowers called.");
		ToUser out = null;
		try {
			// get the user ids!
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(toFol.id), toFol.cursor); 
			long[] ids = IDvals.getIDs();
			System.out.println("incoming id list in TwitterRequestHandler.getFollowers: ");
			for (long id : ids){
				System.out.print(id+" ");
			}
			System.out.println();
			String[] sIds = new String[ids.length];
			int i = 0;
			if (verbose){
				System.out.print("values to be strung: ");
				for (long vId : ids){
					System.out.print(vId+" ");
					sIds[i++] = Long.toString(vId);
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

		} catch (NumberFormatException e) {
			try {
				throw new BadIDException("Bad id given to getFollowers: " + toFol.id);
			} catch (NullPointerException f) {
				throw new BadIDException("Null value given to getFollowers as an id");
			}
		}
		return out;
	}

	// static ArrayList<User> getFollowers(User u) throws BadIDException,
	// TwitterException {
	// ArrayList<User> out = new ArrayList<User>();
	// ArrayList<long[]> pages = new ArrayList<long[]>();
	// try {
	// long cursor = -1;
	// while (cursor != 0) {
	// IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), cursor);
	// pages.add(IDvals.getIDs());
	// cursor = IDvals.getNextCursor();
	// }
	// for (long[] p : pages) {
	// for (long l : p) {
	// try{
	// TwitterUser t = getUser(l, u.firstDepth + 1);
	// out.add(t);
	// } catch(TwitterException e){
	// if (e.getErrorCode() == 50){
	// System.err.println("User could not be found:"+l+"\ncontinuing
	// collection.");
	// }
	// else if (e.getErrorCode() == 63) {
	// System.err.println("User "+l+" has been suspended and ignored.");
	// continue;
	// }
	// else throw e;
	// }
	// }
	// }
	// } catch (NumberFormatException e) {
	// throw new BadIDException("Bad id given to getFollowers: " + u.id);
	// } catch (BadUserException e) {
	// System.err.println("Unusual error with twitter user: " + u.id);
	// }
	// return out;
	// }

	static ToUser getSomeFollowers(ToFollow toFol) throws BadIDException, TwitterException {
		ToUser out = null;
		try {
			IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(toFol.id), toFol.cursor);
			long[] ids = IDvals.getIDs();
			String[] sIds = new String[ids.length];
			int i = 0;
			for (long vId : ids){
				sIds[i++] = Long.toString(vId);
			}
			out = User.sample.new ToUser(sIds, toFol.depth + 1);
		} catch (TwitterException e) {
			if (e.getErrorCode() != -1)
				throw e;
			// otherwise this is just a protected user or we're gettting a
			// similar permissions problem.
		}
		return out;
	}

	// /**
	// *
	// * Returns at most x followers for each call. Less could be called if the
	// user doesn't follow
	// * a lot of people.
	// *
	// * @param u
	// * @param num
	// * @return
	// * @throws BadIDException
	// * @throws TwitterException
	// */
	// static ArrayList<User> getxFollowers(User u, int num) throws
	// BadIDException, TwitterException {
	// ArrayList<User> out = new ArrayList<User>();
	// try {
	// IDs IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), -1);
	// long[] ids = IDvals.getIDs();
	// if (ids.length < num){ //either the user doesn't follow many people or we
	// didn't get enough from twitter
	// if (IDvals.getNextCursor() != 0 ){ //if we can get more from twitter, do
	// LinkedList<long[]> pages = new LinkedList<long[]>();
	// pages.add(ids);
	// while (IDvals.getNextCursor() != 0 && Collected(pages) < num){
	// IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id),
	// IDvals.getNextCursor());
	// ids = IDvals.getIDs();
	// pages.add(ids);
	// }
	// for (long[] page : pages){ // go through and add more users until we're
	// done
	// for(long id : page){
	// if (out.size() < num){
	// try{
	// TwitterUser t = getUser(id, u.firstDepth + 1);
	// out.add(t);
	// } catch(TwitterException e){
	// if (e.getErrorCode() == 50){
	// System.err.println("User could not be found:"+id+"\ncontinuing
	// collection.");
	// }
	// else if (e.getErrorCode() == 63) {
	// System.err.println("User "+id+" has been suspended and ignored.");
	// continue;
	// }
	// else throw e;
	// }
	// }
	// else{ //we're done!
	// return out;
	// }
	// }
	// }
	// }
	// else{ // the user doesn't follow many people. just return who they do
	// follow.
	// for(long id : ids){
	// try{
	// TwitterUser t = getUser(id, u.firstDepth + 1);
	// out.add(t);
	// } catch(TwitterException e){
	// if (e.getErrorCode() == 50){
	// System.err.println("User could not be found:"+id+"\ncontinuing
	// collection.");
	// }
	// else if (e.getErrorCode() == 63) {
	// System.err.println("User "+id+" has been suspended and ignored.");
	// continue;
	// }
	// else throw e;
	// }
	// }
	// }
	// }
	// else{ //we collected enough followers with one getFollowers call! great!
	// for (int i = 0; i < num; i++) {
	// long l = ids[i];
	// try{
	// TwitterUser t = getUser(l, u.firstDepth + 1);
	// out.add(t);
	// } catch(TwitterException e){
	// if (e.getErrorCode() == 50){
	// System.err.println("User could not be found:"+l+"\ncontinuing
	// collection.");
	// }
	// else throw e;
	// }
	// }
	// }
	// } catch (BadUserException e) {
	// System.err.println("Unusual error with twitter user: " + u.id);
	// } catch (TwitterException e){
	// if (e.getErrorCode() != -1) throw e;
	// System.err.println("Unusual error with twitter user: " + u.id);
	// if (u.username != null){
	// System.err.println("username of weird error account: "+ u.username);
	// }
	// else{
	// System.err.println("username of weird error account was null.");
	// }
	// }
	// return out;
	// }

	// /**
	// * @return up to
	// * @throws TwitterException
	// */
	// static String[] getSomeFollowerIds(User u) throws TwitterException{
	// IDs IDvals;
	// try {
	// IDvals = getTwitter().getFollowersIDs(Long.valueOf(u.id), -1);
	// long[] ids = IDvals.getIDs();
	// String[] out = new String[ids.length];
	// int i = 0;
	// for (long id : ids){
	// out[i++] = Long.toString(id);
	// }
	// return out;
	// } catch (NumberFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (TwitterException e){
	// if (e.getErrorCode() != -1) throw e;
	// System.err.println("Unusual error with twitter user: " + u.id);
	// if (u.username != null){
	// System.err.println("username of weird error account: "+ u.username);
	// }
	// else{
	// System.err.println("username of weird error account was null.");
	// }
	// }
	// return null;
	// }

	/**
	 * @see getUsers
	 * 
	 * @param id
	 * @param depth
	 * @return
	 * @throws BadUserException
	 * @throws TwitterException
	 */
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
		} catch (TwitterException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	static void getPosts(User u) throws TwitterException {
		if (u == null) return;
		// TODO fill this out or use twitter4j's user implementation bc there's
		// a lot more to work with!
		Paging paging = new Paging(1, 200); //200 is the max page size for this twitter resource as of june 2016
		List<Status> statuses = getTwitter().getUserTimeline(u.username, paging);
		for (Status s : statuses) {
			// TODO save raw statuses via a new TwitterSample method
			if (verbose) System.out.println("status collected in TwitterRequestHandler.getPosts: "+s.toString());
			Post p = new TwitterStatus(s);
			try {
				u.addPost(p);
			} catch (RedundantEntryException e) {
			}
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
				catch (TwitterException e) {
					if (e.getErrorCode() == 50)
						continue;
					else if (e.getErrorCode() == 63) {
						System.err.println("User " + id + " has been suspended and ignored.");
						continue;
					} else
						throw e;
				}

				User reposter = Post.getSample().users.get(Long.toString(id));
				Repost r = new Repost(p, reposter, p.getAuthor());
				// ^ Action orginates in reposter and goes to poster
				reposter.getTensors().add(r);
			}
		}
	}
}

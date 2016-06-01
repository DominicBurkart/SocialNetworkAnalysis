package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.LinkedList;

import SocialNetworkAnalysis.Sample.ToUser;
import twitter4j.TwitterException;

/**
 * Gives us the abstract framework of a User plus specialized functions for
 * twitter stuff!
 * 
 * @author dominicburkart
 */
public class TwitterUser extends User {

	Location location;
	
	private void checkToLinkFriends(){
		if (TwitterSample.toLinkFriends.containsKey(this.id)){
			LinkedList<User> uL = TwitterSample.toLinkFriends.get(this.id);
			if (verbose) System.out.println("While instantiating user "+this.id+" toLinkFriends detected. number of links: "+uL.size());
			for (User u : uL){
				new Follow(u, this);
			}
		}
	}

	public TwitterUser(String username, String id, int depth) throws RedundantEntryException {
		super(username, id, depth);
		checkToLinkFriends();
	}

	public TwitterUser(String id, int depth) throws RedundantEntryException {
		super(id, depth);
		checkToLinkFriends();
	}

	@Override
	public ToUser getFollowers() throws APIException {
		try {
			return TwitterRequestHandler.getFollowers(User.sample.new ToFollow(this));
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getFollowers() recieved a bad ID: " + this.id);
			return null; // TODO double check that this is fine
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}

	public ToUser getFriends() throws APIException {
		try {
			return TwitterRequestHandler.getFriends(User.sample.new ToFollow(this));
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getFollowers() recieved a bad ID: " + this.id);
			return null; // TODO double check that this is fine
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}

	@Override
	public ArrayList<Post> getPosts() throws APIException {
		try {
			TwitterRequestHandler.getPosts(this);
		} catch (TwitterException e) {
			throw new APIException(e);
		}
		return posts;
	}

	// @Override
	// public ArrayList<User> getSomeFollowers() throws APIException {
	// try {
	// return TwitterRequestHandler.getSomeFollowers(this);
	// } catch (BadIDException e) {
	// System.err.println("TwitterUser.getSomeFollowers() recieved a bad ID: " +
	// this.id);
	// System.err.println("Saving and quitting.");
	// sample.usersToTSV();
	// System.exit(1);
	// return null; // for the compiler
	// } catch (TwitterException e) {
	// throw new APIException(e);
	// }
	// }

	// @Override
	// public ToUser getxFollowers(int n) throws APIException{
	// try {
	// return TwitterRequestHandler.getxFollowers(this, n);
	// } catch (BadIDException e) {
	// System.err.println("TwitterUser.getxFollowers() recieved a bad ID: " +
	// this.id);
	// System.err.println("Saving and quitting.");
	// sample.usersToTSV();
	// System.exit(1);
	// return null; // for the compiler
	// } catch (TwitterException e) {
	// throw new APIException(e);
	// }
	// }

	@Override
	public String toString() {
		if (location != null) {
			return super.toString() + "\t" + location.toString();
		} else {
			return super.toString() + "\t" + "null";
		}
	}

	/**
	 * JUST FOR REMEMBERING A USER WE SAVED TO CSV USING THE toString() METHOD.
	 * 
	 * @return
	 */
	public TwitterUser(String stringified) {
		super(stringified);
		String[] parts = stringified.split("\t");
		location = new Location(parts[parts.length - 1]);
	}
}
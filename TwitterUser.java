package SocialNetworkAnalysis;

import java.util.ArrayDeque;

import SocialNetworkAnalysis.Sample.ToUser;
import twitter4j.TwitterException;

/**
 * Gives us the abstract framework of a User plus specialized functions for
 * twitter network stuff! Needs to be used with the REST API / an instantiated 
 * TwitterSample object.
 * 
 * @author dominicburkart
 */
public class TwitterUser extends User {

	Location location;
	
	private void checkToLinks(){
		if (TwitterSample.toLinkFriends.containsKey(this.id)){
			ArrayDeque<User> uL = TwitterSample.toLinkFriends.get(this.id);
			if (verbose) System.out.println("While instantiating user "+this.id+" toLinkFriends detected. number of links: "+uL.size());
			for (User u : uL){
				new Follow(u, this);
			}
			TwitterSample.toLinkFriends.remove(this.id); //keeps toLinkFriends small.
		}
		if (TwitterSample.toLinkFollowers.containsKey(this.id)){
			ArrayDeque<User> uL = TwitterSample.toLinkFollowers.get(this.id);
			if (verbose) System.out.println("While instantiating user "+this.id+" toLinkFollowers detected. number of links: "+uL.size());
			for (User u : uL){
				new Follow(this, u);
			}
			TwitterSample.toLinkFollowers.remove(this.id); 
		}
	}

	public TwitterUser(String username, String id, int depth) throws RedundantEntryException {
		super(username, id, depth);
		checkToLinks();
	}

	public TwitterUser(String id, int depth) throws RedundantEntryException {
		super(id, depth);
		checkToLinks();
	}

	public ToUser getFriends() throws APIException {
		try {
			return TwitterRequestHandler.getFriends(User.sample.new ToFollow(this));
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getFollowers() recieved a bad ID: " + this.id);
			return null;
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}

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
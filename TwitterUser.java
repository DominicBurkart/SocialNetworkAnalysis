package SocialNetworkAnalysis;

import java.util.ArrayList;

import twitter4j.TwitterException;

public class TwitterUser extends User {

	Location location;

	public TwitterUser(String username, String id, int depth) throws RedundantEntryException {
		super(username, id, depth);
	}

	public TwitterUser(String id, int depth) throws RedundantEntryException {
		super(id, depth);
	}

	@Override
	public ArrayList<User> getFollowers() throws APIException {
		try {
			return TwitterRequestHandler.getFollowers(this);
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

	@Override
	public ArrayList<User> getSomeFollowers() throws APIException {
		try {
			return TwitterRequestHandler.getSomeFollowers(this);
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getSomeFollowers() recieved a bad ID: " + this.id);
			System.err.println("Saving and quitting.");
			sample.usersToCSV();
			System.exit(1);
			return null; // for the compiler
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}
}

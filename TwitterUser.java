package SocialNetworkAnalysis;

import java.util.ArrayList;

import twitter4j.TwitterException;

/**
 * Gives us the abstract framework of a User plus specialized functions for twitter stuff!
 * 
 * @author dominicburkart
 */
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
	
	@Override
	public ArrayList<User> getxFollowers(int n) throws APIException{
		try {
			return TwitterRequestHandler.getxFollowers(this, n);
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getxFollowers() recieved a bad ID: " + this.id);
			System.err.println("Saving and quitting.");
			sample.usersToCSV();
			System.exit(1);
			return null; // for the compiler
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}

	@Override
	Attribute[] getAttributes() {
		String[] s = {"id", "username", "location", "depth"};
		Object[] o = {id, username, location, firstDepth};
		return Attribute.batchMaker(s, o);
	}
	
	public String toString(){
		String s = "";
		for (Attribute a: getAttributes()){
			if (a.v != null){
				if (s == null) s = a.v.toString();
				else s = s + '\t' + a.v.toString();
			}
			else{
				if (s == null) s = "\t";
				else s = s + '\t';
			}
		}
		return s;
	}
}
package SocialNetworkAnalysis;

import java.util.ArrayList;

public class TwitterUser extends User { 
	
	Location location;

	public TwitterUser(String username, String id, int depth) throws RedundantEntryException{
		super(username, id, depth);
	}
	
	public TwitterUser(String id, int depth) throws RedundantEntryException{
		super(id, depth);
	}

	@Override
	public ArrayList<User> getFollowers() {
		try {
			return TwitterRequestHandler.getFollowers(this);
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getFollowers() recieved a bad ID: "+this.id);
			return null; //TODO double check that this is fine
		}
	}

	@Override
	public ArrayList<Post> getPosts() {
		// TODO Auto-generated method stub
		return null;
	}
}

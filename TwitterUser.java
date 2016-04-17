package SocialNetworkAnalysis;

import java.util.ArrayList;

public class TwitterUser extends User { 
	
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
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<Post> getPosts() {
		// TODO Auto-generated method stub
		return null;
	}
}

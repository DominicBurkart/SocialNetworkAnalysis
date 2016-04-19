package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class User {
	static Sample sample;
	
	ArrayList<Interaction> tensors = new ArrayList<Interaction>();
	
	ArrayList<Post> posts = new ArrayList<Post>();
	String description;
	
	int firstDepth = 0;
	
	String id = null;
	String username = null;
	
	public abstract ArrayList<User> getFollowers();
	public abstract ArrayList<Post> getPosts();
	
	public User(String id, int depth) throws RedundantEntryException{
		if (sample.users.containsKey(id)) throw new RedundantEntryException("User with id "+id+" already exists.");
		sample.users.put(id, this);
		firstDepth = depth;
	}
	
	public User(String username, String id, int depth) throws RedundantEntryException{
		this(id, depth);
		this.username = username;
	}
	
	public void setDesc(String desc){
		this.description = desc;
	}
	
	public void addPost(Post p) throws RedundantEntryException{
		if (posts.contains(p)) throw new RedundantEntryException("Post already exists in given user.");
		posts.add(p);
	}
	
	public void addPosts(Collection<Post> p) throws RedundantEntryException{
		Iterator<Post> pI = p.iterator();
		while (pI.hasNext()){
			if (posts.contains(pI.next())) throw new RedundantEntryException("At least one input post already exists in given user.");
		}
		posts.addAll(p);
	}
	
	public void findAllPostInteractions(){
		for (Post p : this.posts){
			p.getAssociatedInteractions();
			this.tensors.addAll(p.associatedInteractions);
		}
	}
}
package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Allows for the manipulation of User objects collected from any supported
 * site.
 * 
 * @author dominicburkart
 */
public abstract class User extends SNA_Root {
	public static Sample sample;

	Tensors tensors = new Tensors();
	
	ArrayList<Post> posts = new ArrayList<Post>();
	public String description = "";

	public int firstDepth = 0;

	public String id = "";
	public String username = "";
	
	public boolean incomplete;
	
	public boolean fromPost;
	
	public boolean fromFromPost;
	//users who were partially collected from tweets by users collected from posts from users who fit
	// in the primary scope of the collection.
	
	public boolean postsCollected;
	
	private User old; //for building a complete account piecemeal.
	
	public class Tensors {
		ArrayList<Like> likes = new ArrayList<Like>();
		ArrayList<Repost> reposts = new ArrayList<Repost>();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		ArrayList<Follow> follows = new ArrayList<Follow>();
		ArrayList<Follow> friends = new ArrayList<Follow>(); //people who this user follows.
		ArrayList<Mention> mentions = new ArrayList<Mention>();

		ArrayList<Interaction> getTensors() {
			ArrayList<Interaction> tensors = new ArrayList<Interaction>();
			tensors.addAll(follows);
			tensors.addAll(friends);
			tensors.addAll(reposts);
			tensors.addAll(mentions);
			tensors.addAll(comments);
			tensors.addAll(likes);
			return tensors;
		}

		public void addAll(ArrayList<Interaction> interactions) {
			for (Interaction i : interactions) {
				this.add(i);
			}
			posts.trimToSize();
		}

		public void add(Interaction i) {
			switch (i.type) {
			case "follow":
				if (i.source.id.equals(id)){
					friends.add((Follow) i);
				}
				else{
					follows.add((Follow) i);
				}
				break;
			case "like":
				likes.add((Like) i);
				break;
			case "repost":
				reposts.add((Repost) i);
				break;
			case "comment":
				comments.add((Comment) i);
				break;
		}
		}
	}

	public User(String id, int depth) {
		if (id.equals("-1") || id.equals("") || id.equals("0"))
			throw new IllegalArgumentException();
		if (sample != null){
			old = sample.users.get(id);
			sample.users.put(id, this);
			if (old == null || old.incomplete)
				firstDepth = depth;
			else
				firstDepth = old.firstDepth;
		}else{
			firstDepth = depth;
		}
		this.id = id;
	}

	public User(String username, String id, int depth) {
		this(id, depth);
		this.username = username;
	}

	public void setDesc(String desc) {
		if (desc != null && desc.length() > 0)
			this.description = desc;
	}

	public void addPost(Post p) throws RedundantEntryException {
		if (posts.contains(p))
			throw new RedundantEntryException("Post already exists in given user.");
		posts.add(p);
	}

	public void addPosts(Collection<Post> p){
		Iterator<Post> pI = p.iterator();
		while (pI.hasNext()) {
			try {
				this.addPost(pI.next());
			} catch (RedundantEntryException e) {
			}
		}
	}

	public void findAllPostInteractions() {
		for (Post p : this.posts) {
			p.getAssociatedInteractions();
			this.getTensors().addAll(p.associatedInteractions);
		}
	}

	@Override
	public String toString() {
		return (username.toString() + "\t" + id.toString() + "\t"
				+ firstDepth + "\t" + description.toString());
	}

	public User() {
	}

	public Tensors getTensors() {
		return tensors;
	}

	public void setTensors(Tensors tensors) {
		this.tensors = tensors;
	}
}
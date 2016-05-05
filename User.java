package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Allows for the manipulation of User objects collected from any supported site.
 * 
 * @author dominicburkart
 */
public abstract class User extends Attributional{
	static Sample sample;
	
	Tensors tensors = new Tensors();

	public class Tensors {
		ArrayList<Like> likes = new ArrayList<Like>();
		ArrayList<Repost> reposts = new ArrayList<Repost>();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		ArrayList<Follow> follows = new ArrayList<Follow>();

		ArrayList<Interaction> getTensors() {
			ArrayList<Interaction> tensors = new ArrayList<Interaction>();
			tensors.addAll(follows);
			tensors.addAll(reposts);
			tensors.addAll(comments);
			tensors.addAll(likes);
			return tensors;
		}

		public void addAll(ArrayList<Interaction> interactions) {
			for (Interaction i : interactions) {
				this.add(i);
			}
		}

		public void add(Interaction i) {
			switch (i.type) {
			case "follow":
				follows.add((Follow) i);
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

	ArrayList<Post> posts = new ArrayList<Post>();
	String description;

	int firstDepth = 0;

	String id = null;
	String username = null;

	public abstract ArrayList<User> getFollowers() throws APIException;

	public abstract ArrayList<User> getSomeFollowers() throws APIException;
	
	public abstract ArrayList<User> getxFollowers(int x) throws APIException;

	public abstract ArrayList<Post> getPosts() throws APIException;

	public User(String id, int depth) throws RedundantEntryException {
		if (sample.users.containsKey(id))
			throw new RedundantEntryException("User with id " + id + " already exists.", sample.users.get(id));
		sample.users.put(id, this);
		firstDepth = depth;
		this.id = id;
	}

	public User(String username, String id, int depth) throws RedundantEntryException {
		this(id, depth);
		this.username = username;
	}

	public void setDesc(String desc) {
		this.description = desc;
	}

	public void addPost(Post p) throws RedundantEntryException {
		if (posts.contains(p))
			throw new RedundantEntryException("Post already exists in given user.");
		posts.add(p);
	}

	public void addPosts(Collection<Post> p) throws RedundantEntryException {
		Iterator<Post> pI = p.iterator();
		while (pI.hasNext()) {
			try {
				this.addPost(pI.next());
			} catch (RedundantEntryException e) {
			}
		}
		posts.addAll(p);
	}

	public void findAllPostInteractions() {
		for (Post p : this.posts) {
			p.getAssociatedInteractions();
			this.tensors.addAll(p.associatedInteractions);
		}
	}

	@Override
	public String toString() {
		return (username.toString() + "\t" + id.toString() + "\t" + description.toString() + "\t" + firstDepth);
	}
	
	public User(){}
	
	/**	EXCLUSIVELY FOR MAKING A USER OBJECT FROM A 
	 *  STRING MADE BY THE toString() METHOD.
	 */
	public User(String stringified){
		String s = stringified;
		int tab1 = s.indexOf('\t');
		username = s.substring(0, tab1);
		int tab2 = s.indexOf('\t', tab1+1);
		id = s.substring(tab1+1, tab2);
		tab1 = s.indexOf('\t', tab2+1);
		description = s.substring(tab2+1, tab1);
		firstDepth = Integer.parseInt(s.substring(tab1+1));
	}
}
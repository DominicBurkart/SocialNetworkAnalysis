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
	
	public class Tensors {
		ArrayList<Like> likes = new ArrayList<Like>();
		ArrayList<Repost> reposts = new ArrayList<Repost>();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		ArrayList<Follow> follows = new ArrayList<Follow>();
		ArrayList<Follow> friends = new ArrayList<Follow>(); //people who this user follows.

		ArrayList<Interaction> getTensors() {
			ArrayList<Interaction> tensors = new ArrayList<Interaction>();
			tensors.addAll(follows);
			tensors.addAll(friends);
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
			this.getTensors().addAll(p.associatedInteractions);
		}
	}

	@Override
	public String toString() {
		return (username.toString() + "\t" + id.toString() + "\t" + Utilities.cleanstring(description.toString()) + "\t"
				+ firstDepth);
	}

	public User() {
	}

	/**
	 * EXCLUSIVELY FOR MAKING A USER OBJECT FROM A STRING MADE BY THE toString()
	 * METHOD.
	 */
	public User(String stringified) {
		String s = stringified;
		int tab1 = s.indexOf('\t');
		username = s.substring(0, tab1);
		int tab2 = s.indexOf('\t', tab1 + 1);
		id = s.substring(tab1 + 1, tab2);
		tab1 = s.indexOf('\t', tab2 + 1);
		description = s.substring(tab2 + 1, tab1);
		firstDepth = Integer.parseInt(s.substring(tab1 + 1));
	}

	public Tensors getTensors() {
		return tensors;
	}

	public void setTensors(Tensors tensors) {
		this.tensors = tensors;
	}
}
package SocialNetworkAnalysis;

import java.sql.Timestamp;
import java.util.ArrayList;

public abstract class Post implements Comparable<Post> {
	static Sample sample; 
	
	String id;
	ArrayList<Interaction> associatedInteractions;
	Timestamp time;
	String message;
	User author;
	boolean original; //reposts have original as false.
	User originalAuthor;
	User repostedFrom;
	int notes;
	String comment;
	ArrayList<String> tags;
	
	public Post(){
		sample.allPosts.add(this);
	}
	
	public Post(String id, Timestamp time, String message, int notes, String comment, ArrayList<String> tags, Interaction associatedInteraction, boolean original, User author, User repostedFrom, User originalAuthor){
		this.id = id;
		this.time = time;
		this.message = message;
		this.notes = notes;
		this.comment = comment;
		this.tags = tags;
		this.author = author;
		this.repostedFrom = repostedFrom;
		this.originalAuthor = originalAuthor;
		this.original = original;
		associatedInteractions = new ArrayList<Interaction>();
		associatedInteractions.add(associatedInteraction);
	}
	
	public Post(String id, Timestamp time, String message, int notes, String comment, ArrayList<String> tags, ArrayList<Interaction> associatedInteractions, boolean original, User author, User repostedFrom, User originalAuthor){
		this.id = id;
		this.time = time;
		this.message = message;
		this.notes = notes;
		this.comment = comment;
		this.tags = tags;
		this.author = author;
		this.repostedFrom = repostedFrom;
		this.originalAuthor = originalAuthor;
		this.associatedInteractions = associatedInteractions;
	}
	
	public Post(String id, Timestamp time, String message, int notes, ArrayList<String> tags, User author){
		this.id = id;
		this.time = time;
		this.message = message;
		this.notes = notes;
		this.tags = tags;
		this.author = author;
		this.original = true;
	}
	
	/** Returns 0 if the posts have the same id, otherwise returns the difference between this post's notes versus the other's.
	 * @return
	 */
	public int CompareTo(Post other){
		if (other.id.equals(this.id)) return 0;
		return this.notes - other.notes;
	}
}

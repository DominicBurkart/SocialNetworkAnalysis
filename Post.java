package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Date;

public abstract class Post implements Comparable<Post> {
	static Sample sample; 
	
	String id;
	ArrayList<Interaction> associatedInteractions;
	Date time;
	String message;
	User author;
	boolean original; //reposts have original as false.
	User originalAuthor;
	User repostedFrom;
	int notes;
	String comment;
	ArrayList<String> tags;
	Location location;
	
	public Post(){
		sample.allPosts.add(this);
	}
	
	/**
	 * @return 0 if the posts have the same id, otherwise returns the difference between this post's notes versus the other's.
	 */
	public int CompareTo(Post other){
		if (other.id.equals(this.id)) return 0;
		return this.notes - other.notes;
	}

	public void getAssociatedInteractions() {
		if (!this.original){
			User target = this.originalAuthor;
			User source = this.author;
			new Repost(this, source, target);
		}
		
	}
}

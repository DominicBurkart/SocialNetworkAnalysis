package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Date;

/**
 * Abstract class to refer to a post on a social network.
 * @author dominicburkart
 */
public abstract class Post extends Attributional implements Comparable<Post> {
	static Sample sample;

	String id;
	ArrayList<Interaction> associatedInteractions;
	Date time;
	String message;
	User author;
	boolean original; // reposts have original as false.
	User originalAuthor;
	User repostedFrom;
	int notes;
	String comment;
	ArrayList<String> tags;
	Location location;
	String site; //eg "twitter" or "tumblr"
	
	public Post(){}
	
	public Post(String id, User author, String message) {
		this.id = id;
		this.author = author;
		this.message = message;
		sample.allPosts.put(id, this);
	}

	/**
	 * @return 0 if the posts have the same id, otherwise returns the difference
	 *         between this post's notes versus the other's.
	 */
	@Override
	public int compareTo(Post other) {
		if (other.id.equals(this.id))
			return 0;
		return this.notes - other.notes;
	}

	public void getAssociatedInteractions() {
		if (!this.original) {
			User target = this.originalAuthor;
			User source = this.author;
			new Repost(this, source, target);
		}
	}
	
	public Attribute[] getAttributes(){
		String[] s = {"id", "time", "message", "author", "original", "originalAuthor", "repostedFrom", "notes", "comment", "tags", "location", "site"};
		Object[] o = { id ,  time ,  message ,  author ,  original ,  originalAuthor ,  repostedFrom ,  notes ,  comment ,  tags ,  location ,  site};
		return Attribute.batchMaker(s, o);
	}
	
	public String toString(){
		String s = ""; //TODO implement this with StringBuffer across the toString functions after completing current goals
		for (Object o : getAttributes()){
			String val = o.toString();
			if (o.toString().indexOf('\t') != -1){
				throw new IllegalArgumentException("bad value to toString : "+o);
			}
			if (s == null) s = val;
			else s = s + '\t' + val;
		}
		return s;
	}
	
	/** EXCLUSIVELY FOR IMPORTING VALUES
	 * FROM THE STRING VERSION OF A POST
	 * MADE BY THIS CLASS'S toString().
	 */
	@SuppressWarnings("deprecation")
	public Post(String s){
		String [] sa = s.split("\t");
		id = sa[0];
		time = new Date();
		time.setTime(Date.parse(sa[1]));
		message = sa[2];
		author = sample.users.get(sa[3]);
		original = Boolean.parseBoolean(sa[4]);
		originalAuthor = sample.users.get(sa[5]);
		repostedFrom = sample.users.get(sa[6]);
		notes = Integer.parseInt(sa[7]);
		comment = sa[7];
		//tags = sa[8];// TODO fix this tbh!
		location = new Location(sa[9]);
		site = sa[10];
	}
	
	public Post(String s, String delimiter){
		this(s.replace(delimiter, "\t"));
	}
}
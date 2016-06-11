package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Date;

/**
 * Abstract class to refer to a post on a social network.
 * 
 * @author dominicburkart
 */
public abstract class Post extends SNA_Root implements Comparable<Post> {
	public static Sample sample;

	private String id = "unknown";
	ArrayList<Interaction> associatedInteractions;
	private Date time = new Date();
	private String message = "unknown";
	private User author;
	String authorID = "unknown";
	private boolean original; // reposts have original as false.
	User originalAuthor;
	User repostedFrom;
	private int notes = 0;
	String comment = "null";
	ArrayList<String> tags;
	private Location location = new Location();
	String site = ""; // eg "twitter" or "tumblr"

	public Post(String id, User author, String message) {
		this.setId(id);
		this.setAuthor(author);
		this.authorID = author.id;
		this.setMessage(Utilities.cleanstring(message));
		sample.posts.put(id, this);
	}

	public Post(String id, String authorID, String message) {
		this.setId(id);
		this.authorID = authorID;
		this.setMessage(message);
		sample.posts.put(id, this);
	}

	// TODO public Post(String stringified){

	/**
	 * @return 0 if the posts have the same id, otherwise returns the difference
	 *         between this post's notes versus the other's.
	 */
	@Override
	public int compareTo(Post other) {
		if (other.getId().equals(this.getId()))
			return 0;
		return this.getNotes() - other.getNotes();
	}

	public void getAssociatedInteractions() {
		if (!this.isOriginal()) {
			User target = this.originalAuthor;
			User source = this.getAuthor();
			new Repost(this, source, target);
		}
	}

	public String[] getAttributes() {
		String username = "null";
		if (author != null) {
			username = author.username;
		}
		String oAuthor = "null";
		if (originalAuthor != null) {
			oAuthor = originalAuthor.id;
		}
		String rFrom = "null";
		if (repostedFrom != null) {
			rFrom = repostedFrom.id;
		}
		String[] ats = { id, username, time.toString(), message, authorID, Boolean.toString(isOriginal()), oAuthor,
				rFrom, Integer.toString(getNotes()), comment, Utilities.tagString(tags), getLocation().toString(),
				site };
		return ats;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (String o : getAttributes()) {
			String val = Utilities.cleanstring(o.toString());
			if (s.length() != 0)
				s.append('\t');
			s.append(val);
		}
		return s.toString();
	}

	/**
	 * EXCLUSIVELY FOR IMPORTING VALUES FROM THE STRING VERSION OF A POST MADE
	 * BY THIS CLASS'S toString().
	 */
	@SuppressWarnings("deprecation")
	public Post(String s) {
		String[] sa = s.split("\t");
		setId(sa[0]);
		setTime(new Date());
		getTime().setTime(Date.parse(sa[1]));
		setMessage(sa[2]);
		setAuthor(sample.users.get(sa[3]));
		setOriginal(Boolean.parseBoolean(sa[4]));
		originalAuthor = sample.users.get(sa[5]);
		repostedFrom = sample.users.get(sa[6]);
		setNotes(Integer.parseInt(sa[7]));
		comment = sa[7];
		// tags = sa[8];// TODO fix this tbh!
		setLocation(new Location(sa[9]));
		site = sa[10];
	}

	public Post(String s, String delimiter) {
		this(s.replace(delimiter, "\t"));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNotes() {
		return notes;
	}

	public void setNotes(int notes) {
		this.notes = notes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = Utilities.cleanstring(message);
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public boolean isOriginal() {
		return original;
	}

	public void setOriginal(boolean original) {
		this.original = original;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public static Sample getSample() {
		return sample;
	}
}
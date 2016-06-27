package SocialNetworkAnalysis;

import java.util.ArrayList;

/**
 * Class used to denote a comment tensor between two users,
 * based on the source adding commentary to a specific post
 * of the target.
 * 
 * @author dominicburkart
 */
public class Comment extends Interaction {
	Post post;
	String comment = "null";
	ArrayList<String> tags = new ArrayList<String>();
	String type = "comment";

	public Comment(Post comment, User source, User target) {
		this.post = comment;
		this.comment = post.comment;
		this.post.associatedInteractions.add(this);
		this.tags = post.tags;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		if (post != null) {
			s.append('\t' + post.getId());
		} else {
			s.append("\tnull");
		}
		s.append('\t' + comment);
		s.append('\t' + Utilities.tagString(tags));
		return super.toString() + s.toString();
	}
}

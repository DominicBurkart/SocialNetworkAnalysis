package SocialNetworkAnalysis;

import java.util.ArrayList;

//TODO: more fully implement this.
public abstract class Comment extends Interaction {
	Post post;
	String comment;
	ArrayList<String> tags;
	String type = "comment";

	public Comment(Post post) {
		this.post = post;
		this.comment = post.comment;
		this.post.associatedInteractions.add(this);
		this.tags = post.tags;
	}
}

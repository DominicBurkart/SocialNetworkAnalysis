package SocialNetworkAnalysis;

/**
 * Refers to a user (source) reposting the
 * content of another user (target).
 * 
 * @author dominicburkart
 */
public class Repost extends Interaction {
	Post post;

	public Repost(Post post, User source, User target) {
		this.post = post;
		this.source = source;
		this.target = target;
		associatedPostID = post.getId();
		post.associatedInteractions.add(this);
		this.source.getTensors().reposts.add(this);
		sample.allInteractions.add(this);
		type = "repost";
	}

	@Override
	public String toString() {
		return (super.toString());
	}
}

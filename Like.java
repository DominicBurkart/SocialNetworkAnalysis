package SocialNetworkAnalysis;

/**
 * Denotes that a user (source) "liked" or "favorited"
 * a specific post from another user (target).
 * @author dominicburkart
 */
public class Like extends Interaction {
	Post post;

	public Like(Post post, User source, User target) {
		this.type = "like";
		this.source = source;
		this.target = target;
		this.post = post;
		post.associatedInteractions.add(this);
	}

	@Override
	public String toString() {
		if (post != null)
			return (super.toString() + '\t' + post.getId() + '\t' + post.getMessage());
		else
			return (super.toString() + '\t' + "null" + '\t' + "null");
	}
}

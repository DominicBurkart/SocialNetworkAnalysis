package SocialNetworkAnalysis;

public abstract class Like extends Interaction {
	Post post;

	public Like(Post post, User source, User target) {
		this.type = "like";
		this.source = source;
		this.target = target;
		this.post = post;
		post.associatedInteractions.add(this);
	}
}

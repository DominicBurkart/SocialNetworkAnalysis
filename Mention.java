package SocialNetworkAnalysis;

public class Mention extends Interaction {
	Post post;

	public Mention(Post post, User source, User target) {
		this.post = post;
		this.source = source;
		this.target = target;
		associatedPostID = post.getId();
		post.associatedInteractions.add(this);
		this.source.getTensors().mentions.add(this);
		sample.allInteractions.add(this);
		type = "mention";
	}

	@Override
	public String toString() {
		return (super.toString());
	}
}

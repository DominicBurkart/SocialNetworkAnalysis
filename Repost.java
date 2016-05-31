package SocialNetworkAnalysis;

/**
 * Refers to a user (source) reposting the
 * content of another user (target).
 * 
 * @author dominicburkart
 */
public class Repost extends Interaction {
	String type = "repost";
	Post post;

	public Repost(Post post, User source, User target) {
		this.post = post;
		this.source = source;
		this.target = target;
	}

	public Repost() {
	}

	public Repost(String stringified) {
		super(stringified); // takes care of source and target
		String[] split = stringified.split("\t");
		post = sample.posts.get(split[split.length - 1]);
	}

	@Override
	public String toString() {
		return (super.toString() + '\t' + post.getId());
	}
}

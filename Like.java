package SocialNetworkAnalysis;

public class Like extends Interaction {
	Post post;

	public Like(Post post, User source, User target) {
		this.type = "like";
		this.source = source;
		this.target = target;
		this.post = post;
		post.associatedInteractions.add(this);
	}
	
	public Like(String stringified){
		super(stringified); // gets source, target, and type
		String[] attributes = stringified.split("\t");
		//ordering: source, target, type, post id, post message
		this.post = sample.posts.get(attributes[3]);
		System.out.println("like imported: "+this.toString());
	}
	
	public String toString(){
		if (post != null) return (super.toString() + '\t'+ post.getId() +'\t' + post.getMessage());
		else return (super.toString() + '\t' + "null" + '\t' + "null");
	}
}

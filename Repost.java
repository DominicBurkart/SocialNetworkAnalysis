package SocialNetworkAnalysis;

public class Repost extends Interaction {
	String type = "repost";
	Post post;
	
	public Repost(Post post, User source, User target){
		this.post = post;
		this.source = source;
		this.target = target;
	}
	
	public Repost(){
	}
}

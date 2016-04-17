package SocialNetworkAnalysis;

public abstract class Follow extends Interaction{
	public Follow(User source, User target){
		this.source = source;
		this.target = target;
		type = "follow";
		sample.allFollows.add(this);
	}
}

package SocialNetworkAnalysis;

public abstract class Interaction {
	User source;
	User target;
	String type;
	static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}
}

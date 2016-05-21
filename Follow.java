package SocialNetworkAnalysis;

/**
 * An object that denotes that the source user is following the target user.
 * Extends "Interaction," the abstract class that describes all interactions
 * between individual users.
 * 
 * @author dominicburkart
 */
public class Follow extends Interaction {
	public Follow(User source, User target) {
		this.source = source;
		this.target = target;
		type = "follow";
		sample.follows.add(this);
	}

	/**
	 * ONLY FOR USE WITH OUTPUT FROM INTERACTION'S toString().
	 */
	public Follow(String stringified) {
		super(stringified);
		sample.follows.add(this);
	}

}

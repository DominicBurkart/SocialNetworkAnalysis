package SocialNetworkAnalysis;

/** Abstract class to refer
 * to any directed relationship
 * between two users.
 * 
 * @author dominicburkart
 */
public abstract class Interaction {
	User source;
	User target;
	String type;
	static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}
}

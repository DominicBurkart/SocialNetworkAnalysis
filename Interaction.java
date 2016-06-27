package SocialNetworkAnalysis;

/**
 * Superclass to refer to any directed relationship between two users.
 * 
 * @author dominicburkart
 */
public class Interaction {
	User source;
	User target;
	String associatedPostID = "null";
	String type;
	public static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}

	@Override
	public String toString() {
		String[] ats = { source.id, target.id, type, associatedPostID };
		String s = "";
		for (String at : ats) {
			if (s.equals(""))
				s = at;
			else
				s = s + '\t' + at;
		}
		return s;
	}

}

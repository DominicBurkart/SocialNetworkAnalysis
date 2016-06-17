package SocialNetworkAnalysis;

/**
 * Superclass to refer to any directed relationship between two users.
 * 
 * @author dominicburkart
 */
public class Interaction {
	User source;
	User target;
	String type;
	public static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}

	@Override
	public String toString() {
		String[] ats = { source.id, target.id, type };
		String s = "";
		for (String at : ats) {
			if (s.equals(""))
				s = at;
			else
				s = s + '\t' + at;
		}
		return s;
	}

	/**
	 * ONLY FOR USE WITH A STRING MADE BY THE toString() METHOD OF THIS CLASS.
	 */
	public Interaction(String stringify) {
		this();
		String[] split = stringify.split("\t");
		source = sample.users.get(split[0]);
		target = sample.users.get(split[1]);
		type = split[3];
	}
}

package SocialNetworkAnalysis;


/**
 * Class to refer to any directed
 * relationship between two users.
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
	
	public String toString(){
		String[] ats = {source.id, target.id, type};
		String s = "";
		for (String at : ats){
			if (s.equals("")) s = at;
			else s = s + '\t' + at;
		}
		return s;
	}
	
	/** ONLY FOR USE WITH A STRING MADE BY THE 
	 * toString() METHOD OF THIS CLASS.
	 */
	public Interaction(String stringify){
		this();
		String s = stringify;
		int tab1 = s.indexOf('\t');
		String s1 = s.substring(0, tab1);
		source = sample.users.get(s1);
		int tab2 = s.indexOf('\t');
		s1 = s.substring(tab1 +1, tab2);
		target = sample.users.get(s1);
		type= s.substring(tab2+1);
	}
}

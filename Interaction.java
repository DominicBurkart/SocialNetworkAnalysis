package SocialNetworkAnalysis;


/**
 * Class to refer to any directed
 * relationship between two users.
 * 
 * @author dominicburkart
 */
public class Interaction extends Attributional {
	User source;
	User target;
	String type;
	static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}
	
	public Attribute[] getAttributes(){
		String[] names = {"source id", "target id", "type"};
		Object[] values = {source.id, target.id, type};
		Attribute[] atr = Attribute.batchMaker(names, values);
		return atr;
	}
	
	public String toString(){
		Attribute[] ats = getAttributes();
		String s = "";
		for (Attribute at: ats){
			if (s.equals("")) s = Utilities.cleanstring(at.v.toString());
			else s = s + '\t' + at.v;
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
		source = sample.getUsers().get(s1);
		int tab2 = s.indexOf('\t');
		s1 = s.substring(tab1 +1, tab2);
		target = sample.getUsers().get(s1);
		type= s.substring(tab2+1);
	}
}

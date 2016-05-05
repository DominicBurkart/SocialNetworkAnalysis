package SocialNetworkAnalysis;


/** Abstract class to refer
 * to any directed relationship
 * between two users.
 * 
 * @author dominicburkart
 */
public abstract class Interaction extends Attributional {
	User source;
	User target;
	String type;
	static Sample sample;

	public Interaction() {
		sample.allInteractions.add(this);
	}
	
	public Attribute[] getAttributes(){
		String[] names = {"source", "target", "type"};
		Object[] values = {source, target, type};
		Attribute[] atr = Attribute.batchMaker(names, values);
		return atr;
	}
}

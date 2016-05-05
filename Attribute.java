package SocialNetworkAnalysis;

/** Attribute allows us to group the characteristics we collect about various 
 * entities and phenomenon in an extremely object-oriented fashion. These 
 * boxes are passed up and incorporated into the abstract/high level framework
 * of this package for stuff like file i/o etc.
 * 
 * @author dominicburkart
 */
public class Attribute {
	String n; //name of value (eg "depth" or "location")
	Object  v; //value being stored. Needs to have a toString() function!
	
	public Attribute(String name, Object val){
		n = name;
		v = val;
	}
	
	static Attribute[] batchMaker(String[] s, Object[] o){
		if (s.length != o.length){
			throw new IllegalArgumentException("Labelling mismatch in Attribute.batchMaker. Inputs: "+s+" "+o);
		}
		Attribute[] batch = new Attribute[s.length];
		for (int i = 0; i < batch.length; i++){
			batch[i] = new Attribute(s[i], o[i]);
		}
		return batch;
	}
}

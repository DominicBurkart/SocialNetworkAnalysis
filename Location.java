package SocialNetworkAnalysis;

/**
 * Simple class to hold a geographic location. 
 * 
 * @author dominicburkart
 */
public class Location {
	double latitude;
	double longitude;
	String name;
	String locationType;
	
	public Location(){}
	
	/**
	 * ONLY FOR LOADING AN OBJECT STRINGIFIED 
	 * BY THIS CLASS'S toString() METHOD.
	 */
	public Location(String Stringified){
		String s = Stringified;
		int tab1 = s.indexOf("|||");
		name = s.substring(0, tab1);
		int tab2 = s.indexOf("|||", tab1);
		locationType = s.substring(tab1+1, tab2);
		tab1 = s.indexOf("|||", tab2);
		latitude = Double.parseDouble(s.substring(tab2 +1, tab1));
		tab2 = s.indexOf("|||", tab1);
		longitude = Double.parseDouble(s.substring(tab1 +1, tab2));
	}
	
	public String toString(){
		return (name + "|||" +locationType+ "|||" + latitude + "|||"+longitude);
	}
}
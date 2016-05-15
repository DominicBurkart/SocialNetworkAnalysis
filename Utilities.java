package SocialNetworkAnalysis;

import java.util.ArrayList;

/**
 * Convenient place for methods used across this package! 
 * 
 * @author dominicburkart
 */
public class Utilities extends SNA_Root {
	
	static String cleanstring(String s){
		String clean = s.replaceAll("\\s+"," ");
		return clean;
	}
	
	static String tagString(ArrayList<String> tags){
		if (tags == null || tags.size() == 0) return "null";
		StringBuffer s = new StringBuffer();
		for (String tag : tags){
			if (s.length() != 0) s.append("|||");
			s.append(tag);
		}
		return s.toString();
	}
	
	static ArrayList<String> tagsFromString(String s){
		if (s == null || s.equals("null")) return null;
		String[] tags = s.split("|||");
		ArrayList<String> out = new ArrayList<String>(tags.length);
		for (String tag :tags){
			out.add(tag);
		}
		return out;
	}
}

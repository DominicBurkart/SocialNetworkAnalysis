package SocialNetworkAnalysis;

import java.util.ArrayList;

/**
 * Convenient place for methods used across this package!
 * 
 * @author dominicburkart
 */
public class Utilities extends SNA_Root {

	static String cleanstring(String s) {
		String clean = s.replaceAll("\\s+", " ");
		return clean;
	}

	static String tagString(ArrayList<String> tags) {
		if (tags == null || tags.size() == 0)
			return "null";
		StringBuffer s = new StringBuffer();
		for (String tag : tags) {
			if (s.length() != 0)
				s.append("|||");
			s.append(tag);
		}
		return s.toString();
	}

	static ArrayList<String> tagsFromString(String s) {
		if (s == null || s.equals("null"))
			return null;
		String[] tags = s.split("|||");
		ArrayList<String> out = new ArrayList<String>(tags.length);
		for (String tag : tags) {
			out.add(tag);
		}
		return out;
	}
	
	/**
	 * is given a twitter resource number:
	 * - getUserTimeline = 0
	 * - showUser = 1
	 * - getRetweeterIDs = 2
	 * - getFollowersIDs = 3
	 * - getFriendsIDs = 4
	 * - search = 5
	 * 
	 * and converts it to a family number:
	 *  - posts = 0
	 *  - users = 1
	 *  - following = 2
	 *  
	 *  This is used in making sure that Sample's run() function doesn't call sleeping resources.
	 * 
	 * @param r
	 */
	static int resourceToFamily(int r){
		switch (r){
		case 0: return 0;
		case 1: return 1;
		case 2: return 2;
		case 3: return 2;
		case 4: return 2;
		case 5: return 0;
		default: 
			System.err.println("Bad value passed to resourceToFamily: "+r);
			System.err.println("Quitting without saving.");
			System.exit(0);
			return 0;
		}
	}
}

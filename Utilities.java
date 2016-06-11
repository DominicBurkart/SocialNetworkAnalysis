package SocialNetworkAnalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import SocialNetworkAnalysis.Sample.ToUser;

/**
 * Convenient place for methods used across this package!
 * 
 * @author dominicburkart
 */
public class Utilities extends SNA_Root {

	static String cleanstring(String s) {
		String clean = s.replaceAll("\\s", " ");
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
	
	/**
	 * returns the least value from an array of long values.
	 */
	static long least(long[] vals){
		if (vals.length == 0) throw new IllegalArgumentException("Value input for least function was null.");
		long least = vals[0];
		for (long v : vals){
			if (v < least) least = v;
		}
		return least;
	}
	
	/**
	 * returns the least value from an array of int values.
	 */
	static int least(int[] vals){
		if (vals.length == 0) throw new IllegalArgumentException("Value input for least function was null.");
		int least = vals[0];
		for (int v : vals){
			if (v < least) least = v;
		}
		return least;
	}

	public static void toUserChunker(ToUser ids) {
		// splits one big ToUser object into many small enough to fit
		// into Twitter's batch querying system.
		int total = ids.ids.length;
		int chunkNum = total / 100;
		if (total % 100 > 0) {
			chunkNum++;
		}
		ToUser[] chunks = new ToUser[chunkNum];
		int last = 0;
		int i = 0;
		while (last < total) {
			int next = last + 100;
			if (next > total)
				next = total;
			String[] vals = Arrays.copyOfRange(ids.ids, last, next);
			chunks[i++] = User.sample.new ToUser(vals, ids.depth);
			last = next;
		}
		for (ToUser chunk : chunks) {
			if (verbose) {
				System.out.print("chunk being added to getUserQ in HillaryFollowersFriends.followAction: ");
				for (String id : chunk.ids) {
					System.out.print(id + " ");
				}
				System.out.println("\nlength of chunk: " + chunk.ids.length+"\n");
			}
			User.sample.getUserQ.add(chunk);
		}
	}
	
	/**
	 * @return a string of the current system time plus the given duration (in milliseconds).
	 */
	static public String durationToTimeString(long dur){
		Date d = new Date();
		d.setTime(dur + java.lang.System.currentTimeMillis());
		return d.toString();
	}
}

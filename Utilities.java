package SocialNetworkAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

import SocialNetworkAnalysis.Sample.ToUser;
import twitter4j.TwitterException;

/**
 * Convenient place for methods used across this package!
 * 
 * @author dominicburkart
 */
public class Utilities extends SNA_Root {
	public static final int TWITFOLCALLSIZE = 5000;
	public static final long MONTHSECS = 2592000;

	/**
	 * replaces all escape characters/whitespace
	 * with a single space. returns null when given
	 * null input.
	 */
	public static String cleanstring(String s) {
		if (s != null){
			String clean = s.replaceAll("\\s", " ");
			return clean;
		} else{
			return null;
		}
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
			System.err.println("Quitting.");
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
	
	public static long least(Collection<Long> vals) {
		if (vals.size() == 0) throw new IllegalArgumentException("Value input for least function was null.");
		long least = 0;
		boolean first = true;
		for (long v : vals){
			if (first){
				least = v;
				first = false;
			}
			else if (v < least) least = v;
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
	
	/**
	 * for use with a Sample-based program. Chunks an overly-large ToUser object
	 * into chunks at or smaller than the correct size for Twitter queries.
	 */
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
				System.out.print("chunk being added to getUserQ in Utilities.toUserChunker: ");
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
	
	/**	 
	 * @return a single string with every element of a string array represented with
	 * a single underscore between them (eg: ["a","b","c"] yields "a_b_c").
	 */
	static public String strFromAr(String[] ar){
		StringBuffer b = new StringBuffer();
		for (String s : ar){
			if (s.length() != 0) b.append("_");
			b.append(s);
		}
		return b.toString();
	}
	
	static public void handleTwitterException(TwitterException e) throws TwitterException{
		if (verbose) System.err.println("attempting to handle twitter exception: "+e.getErrorCode()+" message: "+e.getErrorMessage());
		int code = e.getErrorCode();
		if (code == 404 || code == 17 || code == 34){
			return; //we aren't authorized to view this resource or it was deleted.
		}
		else if (code == 17){
			System.err.println("Error code 17 returned: \"no user matches for specified terms.\"");
		}
		else if (code == 500 || code == 502 || code == 503 || code == 504 || code == 131 || code == 130){
			System.err.println("Internal error "+code+" in Twitter's servers. Sleeping for five minutes before resuming program.");
			sleepFor(5 * 60 * 1000);
			return;
		}
		else if (e.getErrorCode() == 50){
			System.err.println("Error code fifty returned.");
			return;
		}
		else if (e.getErrorCode() == 63) {
			System.err.println("Queried user has been suspended and so was not able to be collected. Continuing");
			return;
		}
		else if (e.exceededRateLimitation() || e.getStatusCode() == 420){
			System.err.println("Exceeded ratelimit. Sleeping thread for fifteen minutes.");
			sleepFor(15 * 60 * 1000);
		}
		if (e.getErrorCode() != -1)
			throw e; //throws everything else that we didn't handle.
	}
	
	
	private static long lastSaved = 0;
	private static final long TWELVEHOURS = 1000 * 60 * 60 * 12;
	
	public static void sleepFor(long sleep) {
		if (sleep <= 0) return;
		//okay, we know that we have to wait and for how long. Save current data and go to sleep.
		Date d = new Date();
		System.out.println("Current time: "+d.toString());
		System.out.println("Waking at: "+ durationToTimeString(sleep));
		if (saveProgress && sleep > 1000 * 60 * 14 && User.sample != null && java.lang.System.currentTimeMillis() - lastSaved > TWELVEHOURS){
			System.out.println("Saving files...");
			//only save files if we're sleeping for more than fourteen minutes, and only every 12 hours.
			User.sample.toTSV();
			System.out.println("Saving complete.");
			//recalculate sleep after saving all of those files.
			sleep -= (java.lang.System.currentTimeMillis() - d.getTime());
			lastSaved = java.lang.System.currentTimeMillis();
		} else if (saveProgress && User.sample != null && TWELVEHOURS * 2 < java.lang.System.currentTimeMillis() - lastSaved){
			System.out.println("Files have not been saved in over 24 hours. Saving files...");
			User.sample.toTSV();
			lastSaved = java.lang.System.currentTimeMillis();
			System.out.println("Saving complete.");
		}
		if (sleep <= 0) return;
		try {
			Thread.sleep(sleep);
			System.out.println("Awake! Continuing collection.");
		} catch (InterruptedException e) {
			System.err.println("System could not sleep for designated period. Continuing program.");
		}
	}
	
	private static Hashtable<Integer, ArrayDeque<ToUser>> toChunk = new Hashtable<Integer, ArrayDeque<ToUser>>();
	
	public static void userCompleter(User u){
		ToUser out = User.sample.new ToUser(u.id, u.firstDepth);
		ArrayDeque<ToUser> d;
		if (toChunk.containsKey((Integer) u.firstDepth)){
			d = toChunk.get((Integer) out.depth);
			d.add(out);
		} else{
			d = new ArrayDeque<ToUser>();
			d.add(out);
			toChunk.put((Integer) out.depth, d);
		}
		if (d.size() >= 100){
			out = manyToUserToOne(d);
			User.sample.getUserQ.add(out);
			toChunk.remove((Integer) out.depth);
		}
		if (User.sample.completed()){
			//to collect stragglers / the remaining users when everything else is done
			User.sample.getUserQ.addAll(manyToUserToOne());
			//clean up + empty the toChunk keySet.
			toChunk = new Hashtable<Integer, ArrayDeque<ToUser>>();
		}
	}
	
	/**
	 * @param c a collection of ToUser single-id objects of the same
	 * depth.
	 * 
	 * @return
	 */
	public static ToUser manyToUserToOne(Collection<ToUser> c){
		String[] ids = new String[c.size()];
		int depth = 0;
		int i = 0;
		for (ToUser to : c){
			if (i == 0) depth = to.depth;
			if (to.depth != depth || to.single == false) throw new IllegalArgumentException();
			ids[i++] = to.id;
		}
		return User.sample.new ToUser(ids, depth);
	}
	
	private static ArrayList<ToUser> manyToUserToOne(){
		Set<Integer> depths = toChunk.keySet();
		ArrayList<ToUser> l = new ArrayList<ToUser>(depths.size());
		for(Integer depth : depths){
			l.add(manyToUserToOne(toChunk.get(depth)));
		}
		return l;
	}

	/**
	 * checks if an unsorted array (param 1) of objects contains
	 * a pointer to the same object pointed to by the second param.
	 * 
	 * @return true when the given object is in the given array.
	 */
	public static boolean arrayContains(Object[] ar, Object o0) {
		for (Object o1 : ar){
			if (o0.equals(o1)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Uses outDir datafield in Sample class if a sample was instantiated, otherwise
	 * outputs files to the working directory.
	 */
	public static PrintWriter fileHandler(String fname){
		try{
			return fileHandler(fname, Post.sample.outDir, false);
		} catch (NullPointerException e){
			return fileHandler(fname, null, false);
		}
	}
	
	public static PrintWriter fileHandler(String fname, String outDir, boolean append) {
		checkTestOut(outDir);
		File outd = null;
		
		if (outDir != null && outDir != "") {
			outd = new File(outDir);
			if (!outd.exists()) {
			    System.out.println("creating directory: " + outDir);
			    outd.mkdirs();
			}
		}
		
		if (!append){
			File f;
			if (outDir != null && outDir != "") {
				f = new File(outd, fname);
			} else {
				f = new File(fname);
			}
			PrintWriter out;
			try {
				out = new PrintWriter(f);
				return out;
			} catch (FileNotFoundException e) {
				System.err.println("File could not be instantiated.");
				return null;
			}
		}
		else{
			FileWriter fw;
			try {
				if (outd != null){
					File output = new File(outd, fname);
					fw = new FileWriter(output, true);
				}
				else{
					fw = new FileWriter(fname, true);
				}
				BufferedWriter bw = new BufferedWriter(fw);
				return new PrintWriter(bw);
			} catch (IOException e) {
				System.err.println("File could not be substantiated. Error: "+e.getMessage());
				e.printStackTrace();
				System.exit(0);
				return null;
			}
		}
	}
	

	public static void checkTestOut(String outDir) {
		if (outDir == null) {
			outDir = "";
			System.out.println("Saving files in the project folder.");
		} else if (outDir != "") {
			File f;
			try {
				File outdir = new File(outDir);
				if (!outdir.exists()) {
				    if (verbose) System.out.println("creating directory: " + outDir);
				    outdir.mkdirs();
				}
				f = new File(outdir, "temp");
				PrintWriter out = new PrintWriter(f);
				out.close();
				f.delete();
			} catch (FileNotFoundException e) {
				System.err.println("Invalid directory given: " + outDir + "\n Saving files to project folder.");
				outDir = "";
			}
		}
	}

	public static long sum(long[] vals) {
		long out = 0;
		for (long val : vals){
			out += val;
		}
		return out;
	}
	
	public static long sum(int[] vals) {
		long out = 0;
		for (long val : vals){
			out += val;
		}
		return out;
	}
}


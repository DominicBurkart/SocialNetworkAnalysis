package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A given sample in which each session of collected data is stored and manipulated.
 * 
 * @author dominicburkart
 */
public abstract class Sample {
	String name = "";
	Hashtable<String, User> users = new Hashtable<String, User>();
	ArrayList<Follow> allFollows = new ArrayList<Follow>();
	Hashtable<String, Post> allPosts = new Hashtable<String, Post>();
	ArrayList<Interaction> allInteractions = new ArrayList<Interaction>(); // includes
																			// allFollows
	String outDir;

	public Sample() {
		User.sample = this;
		Interaction.sample = this;
		Post.sample = this;
	}

	public void toCSV(){
		interactionsToCSV();
		usersToCSV();
		followsToCSV();
		postsToCSV();
	}
	
	public void toCSV(String outDir) {
		this.outDir = outDir;
		interactionsToCSV(); //saving order is arbitrary
		usersToCSV();
		followsToCSV();
		postsToCSV();
	}

	public void interactionsToCSV() {
		PrintWriter w = fileHandler(name + "_interactions.csv");
		w.println("~interactions~");
		for (int i = 0; i < allInteractions.size(); i++) {
			w.println(allInteractions.get(i));
		}
		w.close();
	}

	abstract void usersToCSV();

	abstract void followsToCSV();

	abstract void postsToCSV();
	
	public void loadFromCSV(){
		String dir = System.getProperty("user.dir");
		System.out.println("load from csv()'s dir: "+dir);
		loadFromCSV(dir);
	}
	
	protected class Fwrap implements Comparable<Fwrap>{
		String n;
		File f;
		int ordering;
		
		public Fwrap(File f){
			if (f.getName().endsWith(".csv")){
				this.f = f;
				this.n = f.getName();
				defOrd();
			}
			else{
				System.out.println("Discluding file "+f.getName());
			}
		}
		
		private void defOrd(){
			if (n.endsWith("_users.csv")){
				ordering = 1;
			}
			else if (n.endsWith("_posts.csv")){ //dependent on loading users
				ordering = 2;
			}
			else if (n.endsWith("_follows.csv")){ // dep on users
				ordering = 3;
			}
			else if (n.endsWith("_interactions.csv")){ //dep on posts, follows
				ordering = 4;
			}
			else{
				ordering = 5; //all other files will ordered as 5.
				System.out.println("Discluding file "+n);
			}
		}

		@Override
		public int compareTo(Fwrap o) {
			return this.ordering - o.ordering;
		}
	}
	
	abstract void loadFromCSV(String dir);

	protected PrintWriter fileHandler(String fname) {
		checkTestOut();
		File f;
		if (outDir != null || outDir != "") {
			Path p = Paths.get(outDir);
			p = p.toAbsolutePath();
			f = new File(p + "/" + fname);
		} else {
			f = new File(fname);
		}
		PrintWriter out;
		try {
			out = new PrintWriter(f);
			return out;
		} catch (FileNotFoundException e) {
			System.err.println("Sample.CheckTestOut() failed.");
			return null;
		}
	}

	protected void checkTestOut() {
		if (outDir == null) {
			outDir = "";
			System.out.println("Saving files in the project folder.");
		} else if (outDir != "") {
			File f;
			try {
				Path p = Paths.get(outDir);
				p = p.toAbsolutePath();
				f = new File(p + "/" + "temp");
				PrintWriter out = new PrintWriter(f);
				out.close();
				f.delete();
			} catch (FileNotFoundException e) {
				System.err.println("Invalid directory given: " + outDir + "\n Saving files to project folder.");
				outDir = "";
			}
		}
	}
}
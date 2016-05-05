package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * A given sample in which each session of collected data is stored and manipulated.
 * 
 * @author dominicburkart
 */
public class Sample {
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
		interactionsToCSV();
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

	public void usersToCSV() {
		PrintWriter w = fileHandler(name + "_users.csv");
		w.println("~users~");
		Enumeration<String> keys = users.keys();
		while (keys.hasMoreElements()) {
			w.println(users.get(keys.nextElement()));
		}
		w.close();
	}

	public void followsToCSV() {
		PrintWriter w = fileHandler(name + "_follows.csv");
		w.println("~follows~");
		for (Follow follow : allFollows){
			w.println(follow);
		}
		w.close();
	}

	public void postsToCSV() {
		PrintWriter w = fileHandler(name + "_posts.csv");
		w.println("~posts~");
		for (Post post : allPosts){
			w.println(post);
		}
		w.close();
	}
	
	public void loadFromCSV(){
		String dir = System.getProperty("user.dir");
		System.out.println("load from csv()'s dir: "+dir);
		loadFromCSV(dir);
	}
	
	public void loadFromCSV(String dir){
		File p = new File(Paths.get(dir).toString()); //in case the input path isn't absolute
		File[] directory = p.listFiles();
		if (directory.length == 0){
			System.err.println("Empty path passed to loadFromCSV: "+ dir);
			System.err.println("Qutting.");
			System.exit(0);
		}
		for (File file : directory){
			if (file.getName().endsWith(".csv")){
				System.out.println("found file: "+file.getName());
				try {
					Scanner s = new Scanner(file);
					if (s.hasNextLine()){
						switch (s.nextLine()){
						case "~follows~": importFols(s); break;
						case "~posts~": importPosts(s); break;
						case "~users~": importUsers(s); break;
						case "~interactions~": importInteractions(s); break; //must be called after importFols(s);
						default: System.out.println("File "+file.getName()+" was not incuded.");
						}
						System.out.println("Finished with file "+file.getName());
					}
					else{
						System.out.println("File "+file.getName()+" has no content and was not included.");
					}
					s.close();
				} catch (FileNotFoundException e) {
					System.err.println("File deleted or renamed during operation– "+file.getName());
					System.err.println("File " + file.getName() + " can not be read. Quitting program.");
					System.exit(0);
				}
			}
		}
	}
	
	private void importFols(Scanner s){
		
	}
	
	private void importPosts(Scanner s){
		
	}
	
	private void importUsers(Scanner s){
		
	}
	
	private void importInteractions(Scanner s){
		while (s.hasNextLine()){
			String cur = s.nextLine();
			String[] split = cur.split("\t");
			switch (split[3]){
			case "follow": new Follow(cur); break;
			case "like": new Like(cur); break;
			case "repost": new Repost(cur); break;
			case "comment": new Comment(cur); break;
			}
		}
	}

	private PrintWriter fileHandler(String fname) {
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

	private void checkTestOut() {
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
package SocialNetworkAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;

public class TwitterSample extends Sample {
	
	public void usersToTSV() {
		PrintWriter w = fileHandler(name + "_users.tsv");
		w.println("~users~");
		Enumeration<String> keys = getUsers().keys();
		while (keys.hasMoreElements()) {
			w.println(getUsers().get(keys.nextElement()));
		}
		w.close();
	}
	
	public void followsToTSV() {
		PrintWriter w = fileHandler(name + "_follows.tsv");
		w.println("~follows~");
		for (Follow follow : allFollows){
			w.println(follow);
		}
		w.close();
	}
	
	public void postsToTSV() {
		PrintWriter w = fileHandler(name + "_posts.tsv");
		w.println("~posts~");
		for (String k : allPosts.keySet()){
			w.println(allPosts.get(k));
		}
		w.close();
	}
	
	public void loadFromTSV(String dir){
		File p = new File(Paths.get(dir).toString()); //in case the input path isn't absolute
		File[] directory = p.listFiles();
		if (directory.length == 0){
			System.err.println("Empty path passed to loadFromCSV: "+ dir);
			System.err.println("Qutting.");
			System.exit(0);
		}
		ArrayList<Fwrap> sorted = new ArrayList<Fwrap>();
		for (File file : directory){
			Fwrap w = new Fwrap(file);
			if (w.f != null && w.ordering < 5){
				sorted.add(w);
			}
		}
		Collections.sort(sorted);
		for (Fwrap w : sorted){
			File file = w.f;
			System.out.println("found file: "+file.getName());
			try {
				Scanner s = new Scanner(file);
				if (s.hasNextLine()){
					switch (s.nextLine()){
					case "~follows~": importFols(s); break; 
					case "~posts~": importPosts(s); break;
					case "~users~": importUsers(s); break;
					case "~interactions~": importInteractions(s); break;
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
	
	private void importFols(Scanner s){
		while (s.hasNextLine()){
			new Follow(s.nextLine());
		}
	}
	
	private void importPosts(Scanner s){
		while (s.hasNextLine()){
			new TwitterStatus(s.nextLine());
		}
	}
	
	private void importUsers(Scanner s){
		while (s.hasNextLine()){
			new TwitterUser(s.nextLine());
		}
	}
	
	private void importInteractions(Scanner s){
		if (this.allFollows != null && this.allFollows.size() > 0){
			while (s.hasNextLine()){
				String cur = s.nextLine();
				String[] split = cur.split("\t");
				switch (split[3]){
				case "follow": break; //don't reimport follow objects!
				// case "like": new Like(cur); break; TODO add support for this feature!
				case "repost": new Repost(cur); break;
				// case "comment": new Comment(cur); break; TODO add support for this feature!
				}
			}
		}
		else{
			while (s.hasNextLine()){
				String cur = s.nextLine();
				String[] split = cur.split("\t");
				switch (split[3]){
				case "follow": new Follow(cur); break;
				// case "like": new Like(cur); break;
				case "repost": new Repost(cur); break;
				// case "comment": new Comment(cur); break;
				}
			}
		}
	}
}

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

public class Sample {
	String name = "";
	Hashtable<String, User> users = new Hashtable<String, User>();
	ArrayList<Follow> allFollows = new ArrayList<Follow>();
	ArrayList<Post> allPosts = new ArrayList<Post>();
	ArrayList<Interaction> allInteractions = new ArrayList<Interaction>(); //includes allFollows
	String outDir;
	
	public Sample(){
		User.sample = this;
		Interaction.sample = this;
		Post.sample = this;
	}

	
	public void toCSV(String outDir){
		this.outDir = outDir;
		interactionsToCSV();
		usersToCSV();
		followsToCSV();
		postsToCSV();
	}
	
	public void interactionsToCSV(){
		PrintWriter w = fileHandler(name+"_interactions.csv");
		Interaction inter;
		for(int i = 0; i < allInteractions.size(); i++){
			inter = allInteractions.get(i);
			w.print(inter.source.id);
			w.print(",");
			w.print(inter.target.id);
			w.print(",");
			w.println(inter.type);
		}
		w.close();
	}
	
	
	public void usersToCSV(){
		PrintWriter w = fileHandler(name+"_users.csv");
		User u;
		Enumeration<String> keys =  users.keys();
		while(keys.hasMoreElements()){
			u = users.get(keys.nextElement());
			w.print(u.id);
			w.print(",");
			w.println(u.description);
		}
		w.close();
	}
	
	public void followsToCSV(){
		PrintWriter w = fileHandler(name+"_follows.csv");
		Follow fol;
		for(int i = 0; i < allInteractions.size(); i++){
			fol = allFollows.get(i);
			w.print(fol.source.id);
			w.print(",");
			w.print(fol.target.id);
			w.print(",");
			w.print(fol.source.username);
			w.print(",");
			w.print(fol.target.username);
			w.print(",");
			w.println(fol.type);
		}
		w.close();
	}
	
	public void postsToCSV(){
		PrintWriter w = fileHandler(name+"_posts.csv");
		User u;
		Enumeration<String> ids = users.keys();
		while (ids.hasMoreElements()){
			String id = ids.nextElement();
			u = users.get(id);
			ArrayList<Post> posts = u.getPosts();
			for (int i = 0; i < posts.size(); i++){
				Post p = posts.get(i);
				w.print(p.id);
				w.print(",");
				w.print(p.author.id);
				w.print(",");
				w.print(p.time.toString());
				w.print(",");
				if (p.original){
					w.print("original");
					w.print(",");
					w.print(",");
					w.print(",");
					w.print(",");
					w.print(",");
					w.print(",");
				}
				else{
					w.print("repost");
					w.print(",");
					w.print(p.repostedFrom.id);
					w.print(",");
					w.print(p.repostedFrom.username);
					w.print(",");
					w.print(p.originalAuthor.id);
					w.print(",");
					w.print(p.originalAuthor.username);
					w.print(",");
					w.print(p.comment);
					w.print(",");
				}
				w.print(p.notes);
				w.print(",");
				w.print("\""+p.message+"\"");
				w.print(",");
				w.println(p.tags.toString()); //should this formatting be updated?
			}
		}
	}
	
	private PrintWriter fileHandler(String fname){
		try{
			Path p = Paths.get(outDir);
			p = p.toAbsolutePath();
			File f = new File(p+fname);
			PrintWriter out = new PrintWriter(f);
			return out;
		}
		catch (FileNotFoundException e){
			Scanner in = new Scanner(System.in);
			System.err.println("Invalid directory given: "+outDir);
			System.out.println("Input new directory: ");
			outDir = in.nextLine();
			in.close();
			return fileHandler(fname);
		}
	}
}
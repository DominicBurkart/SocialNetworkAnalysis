package SocialNetworkAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Hashtable;

public class ProfileKeeper {
	static ProfileKeeper t = null;
	static Hashtable<Long, ArrayDeque<Long>> usersPosts = new Hashtable<Long,ArrayDeque<Long>>();
	static String outDir;
	static String name;
	
	
	public static ProfileKeeper getPROFILEKEEPER(){
		if (t == null) t = new ProfileKeeper();
		return t;
	}
	
	public static ProfileKeeper setPOSTKEEPER(Sample s){
		getPROFILEKEEPER();
		outDir = s.outDir;
		name = s.name;
		return t;
	}
	
	public User getProfile(long id){
		try {
			BufferedReader f = new BufferedReader(new FileReader(id2profileFile(id)));
			int index = 1;
			String line;
			while ((line = f.readLine()) != null){
				if (Long.parseLong(line.split("/t")[index]) == id){
					f.close();
					return User.userFromLine(line);
				}
			}
			f.close();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String id2profileFile(long id){
		
	}
}

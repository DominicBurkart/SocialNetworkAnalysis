package SocialNetworkAnalysis;

import java.util.ArrayDeque;
import java.util.Hashtable;
import java.util.Set;

public class PostKeeper {
	static PostKeeper t = null;
	static Hashtable<Long, ArrayDeque<Long>> usersPosts = new Hashtable<Long,ArrayDeque<Long>>();
	static String outDir;
	static String name;
	
	
	public static PostKeeper getPOSTKEEPER(){
		if (t==null) t = new PostKeeper();
		return t;
	}
	
	public static PostKeeper setPOSTKEEPER(Sample s){
		getPOSTKEEPER();
		outDir = s.outDir;
		name = s.name;
		return t;
	}
	
	public Set<Long> users(){
		return usersPosts.keySet();
	}
	
//	public String getPosts(Long id){
//		ArrayDeque<Long> postIDs = usersPosts.get(id);
//		FileReader r = new FileReader(id2postFile(id));
//		for (){
//			for (Long p : postIDs){
//				
//			}
//		}
//	}
	
//	public String id2postFile(Long id){
//		
//	}
	
	public ArrayDeque<Long> getPostIDs(Long userID){
		return usersPosts.get(userID);
	}

	public Set<Long> getUsers() {
		return usersPosts.keySet();
	}
}

package SocialNetworkAnalysis;

public class TwitterStatus extends Post {

	@Override
	public int compareTo(Post o) {
		return super.compareTo(o);
	}
	
	public TwitterStatus(){
		site = "twitter";
	}
	
	public TwitterStatus(String id, User author, String message){
		super(id, author, message);
		site = "twitter";
	}
	
	public TwitterStatus(String stringify){
		super(stringify);
	}
	
	public String toString(){
		return super.toString();
	}
	
}

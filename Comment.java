package SocialNetworkAnalysis;

import java.util.ArrayList;

public class Comment extends Interaction {
	Post post;
	String comment = "null";
	ArrayList<String> tags = new ArrayList<String>();
	String type = "comment";

	public Comment(Post post) {
		this.post = post;
		this.comment = post.comment;
		this.post.associatedInteractions.add(this);
		this.tags = post.tags;
	}
	
	public Comment(String stringified){
		super(stringified); //imports source, target, and type
		String[] s =  stringified.split("\t");
		post = sample.posts.get(s[3]);
		comment = s[4];
		tags = Utilities.tagsFromString(s[5]);
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		if (post != null){
			s.append('\t'+post.getId());
		}
		else{
			s.append("\tnull");
		}
		s.append('\t'+comment);
		s.append('\t'+Utilities.tagString(tags));
		return super.toString() + s.toString();
	}
}

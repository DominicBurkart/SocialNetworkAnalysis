package SocialNetworkAnalysis;

public class Utilities extends SNA_Root {
	static String cleanstring(String s){
		String clean = s.replaceAll("\\s+"," ");
		return clean;
	}
}

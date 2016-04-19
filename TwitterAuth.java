package SocialNetworkAnalysis;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth {
	TwitterFactory tf;
	
	public TwitterAuth(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("C KEY")
		  .setOAuthConsumerSecret("C SECRET")
		  .setOAuthAccessToken("T KEY")
		  .setOAuthAccessTokenSecret("T SECRET");
		tf = new TwitterFactory(cb.build());
	}
	
	public Twitter getInstance(){
		return tf.getInstance();
	}
	
	public TwitterFactory getFactory(){
		return tf;
	}
}

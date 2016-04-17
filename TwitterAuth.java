package SocialNetworkAnalysis;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth {
	TwitterFactory tf;
	
	public TwitterAuth(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("*********************")
		  .setOAuthConsumerSecret("******************************************")
		  .setOAuthAccessToken("**************************************************")
		  .setOAuthAccessTokenSecret("******************************************");
		tf = new TwitterFactory(cb.build());
	}
	
	public Twitter getInstance(){
		return tf.getInstance();
	}
}

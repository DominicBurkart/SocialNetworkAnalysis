package SocialNetworkAnalysis.Applications;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Date;

import SocialNetworkAnalysis.TwitterStatus;
import SocialNetworkAnalysis.Utilities;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class GeoStream implements Runnable{
	static TwitterException twit = new TwitterException("");
	static String outDir = "/Volumes/Burkart/files/twitter_streams";
	double[][] pars;
	Configuration fig;
	
	public GeoStream(double[][] pars, Configuration fig){
		this.pars = pars;
		this.fig = fig;
		if (!new File("/Volumes/Burkart/files/twitter_streams").isDirectory()){
			outDir = "twitter_streams";
		}
	}
	
	private static PrintWriter getFile(String filename){
		if (filename == null || filename == ""){
			System.err.println("bad / no filename passed to getFile. Quitting from streamer.");
			System.exit(0);
		}
		return Utilities.fileHandler(filename, outDir, true);
	}
	
	private String getFileName(){
		Date now = new Date(java.lang.System.currentTimeMillis());
		String d = now.toString();
		String filters = Utilities.strFromAr(pars);
		if (filters.length() > 30){
			filters = filters.substring(0, 20) + "_truncated";
		}
		String tsv = ".tsv";
		return d + "_" +filters + tsv;
	}
	
	/**
     * Main entry of this application.
     *
     * @param args follow(comma separated user ids) track(comma separated filter terms)
     */
    public void run() {
    	final String name = Thread.currentThread().getName();

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	PrintWriter p = getFile(getFileName());
                TwitterStatus s = new TwitterStatus(status);
                p.println(s);
                p.close();
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println(name+": Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println(name+": Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println(name+": Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println(name+": Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
            	if (ex.getClass().equals(twit.getClass())){
            		try {
						Utilities.handleTwitterException((TwitterException) ex);
					} catch (TwitterException e) {
						e.printStackTrace();
						System.out.println(e.getErrorMessage());
						System.out.println("error code: "+e.getErrorCode()+" status code: "+e.getStatusCode());
						System.exit(0);
					}
            	}
            	else{
            		ex.printStackTrace();
            	}
            }
        };
        
        TwitterStream twitterStream;
        
        twitterStream = new TwitterStreamFactory(fig).getInstance();
        
        twitterStream.addListener(listener);
        
        FilterQuery filt = new FilterQuery();
        
        filt.locations(pars);
        
        twitterStream.filter(filt);
    }
}

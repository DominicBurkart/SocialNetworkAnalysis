package SocialNetworkAnalysis;

import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * modified version of Yusuke Yamamoto's PrintFilterStream
 * that allows a program collect a stream of tweets in a
 * distinct thread.
 * 
 * original program: https://github.com/yusuke/twitter4j/blob/master/twitter4j-examples/src/main/java/twitter4j/examples/stream/PrintFilterStream.java
 * 
 * @author Yusuke Yamamoto,
 * 		   Dominic Burkart
 */
public final class TwitterStreamerThread implements Runnable{
	static TwitterException twit = new TwitterException("");
	static String outDir = "/Volumes/Burkart/files/twitter_streams";
	String[] args;
	Configuration fig;
	String passedName;
	private PrintWriter p;
	private String filename;
	
	
	public TwitterStreamerThread(String[] args, Configuration fig){
		this.args = args;
		this.fig = fig;
		if (!new File(outDir).isDirectory()){
			outDir = "twitter_streams";
		}
	}
	
	public TwitterStreamerThread(String[] args, Configuration fig, String name) {
		this.args = args;
		this.fig = fig;
		if (!new File(outDir).isDirectory()){
			outDir = "twitter_streams";
		}
		passedName = name;
	}

	private PrintWriter getFile(String filename){
		if (filename == null || filename == ""){
			System.err.println("bad / no filename passed to getFile. Quitting from streamer.");
			System.exit(0);
		}
		if (p != null && filename != null && filename.equalsIgnoreCase(this.filename)){
			return this.p;
		}
		else if (p != null){
			this.p.close();
		}
		this.p = Utilities.fileHandler(filename, outDir, true);
		this.filename = filename;
		return this.p;
	}
	
	private String getFileName(){
		Date now = new Date(java.lang.System.currentTimeMillis());
		String d = now.toString();
		String tsv = ".tsv";
		if (passedName != null && passedName !="" && !passedName.contains(".")){
			return passedName + d +tsv;
		}
		String filters = Utilities.strFromAr(args);
		if (filters.length() > 30){
			filters = filters.substring(0, 20) + "_truncated";
		}
		return d + "_" +filters + tsv;
	}
	
    /**
     * Main entry of this application.
     *
     * @param args follow(comma separated user ids) track(comma separated filter terms)
     */
    public void run() {
    	final String name = Thread.currentThread().getName();
    	
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.PrintFilterStream [follow(comma separated numerical user ids)] [track(comma separated filter terms)]");
            System.exit(-1);
        }

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	PrintWriter p = getFile(getFileName());
                p.println(new TwitterStatus(status));
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
        ArrayList<Long> follow = new ArrayList<Long>();
        ArrayList<String> track = new ArrayList<String>();
        for (String arg : args) {
            if (isNumericalArgument(arg)) {
                for (String id : arg.split(",")) {
                    follow.add(Long.parseLong(id));
                }
            } else {
                track.addAll(Arrays.asList(arg.split(",")));
            }
        }
        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        String[] trackArray = track.toArray(new String[track.size()]);

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        twitterStream.filter(new FilterQuery(0, followArray, trackArray));
    }

    private static boolean isNumericalArgument(String argument) {
        String args[] = argument.split(",");
        boolean isNumericalArgument = true;
        for (String arg : args) {
            try {
                Integer.parseInt(arg);
            } catch (NumberFormatException nfe) {
                isNumericalArgument = false;
                break;
            }
        }
        return isNumericalArgument;
    }
}
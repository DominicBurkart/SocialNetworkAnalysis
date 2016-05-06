package SocialNetworkAnalysis;

import java.util.LinkedList;

public abstract class IterativeNetworkConstruction {
	int depth;
	static Sample sample;
	String outdir;

	public IterativeNetworkConstruction(Sample s) {
		this(s, 5);
	}

	public IterativeNetworkConstruction(Sample s, int depth) {
		this.depth = depth;
		sample = s;
	}

	public IterativeNetworkConstruction(Sample s, int depth, String outdir) {
		this.depth = depth;
		sample = s;
		User.sample = sample;
		Interaction.sample = sample;
		Post.sample = sample;
		this.outdir = outdir;
	}

	public void run() {
		System.out.println("Running data collection.");
		LinkedList<User> jobs = firstPass();
		System.out.println("First pass completed. " + jobs.size() + " users collected.");
		System.out.println("Initiating iterative collection.");
		while (!jobs.isEmpty()) {
			User u = jobs.removeFirst();
			LinkedList<User> nextPass = subsequentPass(u);
			if (u.firstDepth < this.depth - 1) {
				jobs.addAll(nextPass);
			}
		}
		System.out.println("Iterative collection complete! Saving output files.");
		sample.toTSV(outdir);
		System.out.println("Output files saved. Sampling complete.");
	}

	public abstract LinkedList<User> firstPass(); // yields a list of users from
													// our first sample of posts

	public abstract LinkedList<User> subsequentPass(User u); // does subsequent
																// pass with a
																// user as input

}

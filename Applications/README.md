# Examples + Applications of SocialNetworkAnalysis!
These are the actual scripts that I run while collecting data with this package! -Dominic

### HillaryFollowersFriends / TrumpFollowersFriends
Collects a given number of Twitter users (goal) who follow a given root user (root), as well as everyone that they follow (their "friends"). A sample of tweets is collected for every user queried, from the root to the root's followers to the friends of the followers.

### Hyperstream1, QueerMultiStream
Opens multiple authenticated Twitter streams to collect tweets on a variety of filters simultaneously.

### TwitterFollowersFriends
Does the same thing as HillaryFollowersFriends and TrumpFollowersFriends with whatever user you want.

### TwitterFriendsFriends
Traverses up the Twitter network of a user instead of traversing both down (through a sample of followers) and up.

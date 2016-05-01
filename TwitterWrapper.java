package SocialNetworkAnalysis;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import twitter4j.AccountSettings;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.FavoritesResources;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.api.HelpResources;
import twitter4j.api.ListsResources;
import twitter4j.api.PlacesGeoResources;
import twitter4j.api.SavedSearchesResources;
import twitter4j.api.SearchResource;
import twitter4j.api.SpamReportingResource;
import twitter4j.api.SuggestedUsersResources;
import twitter4j.api.TimelinesResources;
import twitter4j.api.TrendsResources;
import twitter4j.api.TweetsResources;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.util.function.Consumer;

@SuppressWarnings("serial")
public class TwitterWrapper implements Twitter {
	int getUserTimeline = 0;
	Twitter t;
	Ratelimit_Reached limit = new Ratelimit_Reached();
	
	public TwitterWrapper(Twitter t){
		this.t = t;
	}
	
	public void addRatelimitChild(Ratelimit_Reached_Listener b){
		limit.addListener(b);
	}

	@Override
	public AccessToken getOAuthAccessToken() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessToken getOAuthAccessToken(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessToken getOAuthAccessToken(RequestToken arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessToken getOAuthAccessToken(RequestToken arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccessToken getOAuthAccessToken(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestToken getOAuthRequestToken() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestToken getOAuthRequestToken(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestToken getOAuthRequestToken(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestToken getOAuthRequestToken(String arg0, String arg1, String arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOAuthAccessToken(AccessToken arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOAuthConsumer(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public OAuth2Token getOAuth2Token() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidateOAuth2Token() throws TwitterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOAuth2Token(OAuth2Token arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRateLimitStatusListener(RateLimitStatusListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Authorization getAuthorization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getId() throws TwitterException, IllegalStateException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getScreenName() throws TwitterException, IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onRateLimitReached(Consumer<RateLimitStatusEvent> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRateLimitStatus(Consumer<RateLimitStatusEvent> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseList<Status> getHomeTimeline() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getHomeTimeline(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getMentionsTimeline() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getMentionsTimeline(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getRetweetsOfMe(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserTimeline() throws TwitterException {
		if (getUserTimeline == 300){
			
			getUserTimeline = 0;
		}
		getUserTimeline += 1;
		return t.getUserTimeline();
	}

	@Override
	public ResponseList<Status> getUserTimeline(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserTimeline(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserTimeline(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserTimeline(String arg0, Paging arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserTimeline(long arg0, Paging arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status destroyStatus(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OEmbed getOEmbed(OEmbedRequest arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getRetweeterIds(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getRetweeterIds(long arg0, int arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getRetweets(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> lookup(long... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status retweetStatus(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status showStatus(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status updateStatus(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status updateStatus(StatusUpdate arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadedMedia uploadMedia(File arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UploadedMedia uploadMedia(String arg0, InputStream arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryResult search(Query arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectMessage destroyDirectMessage(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getDMImageAsStream(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<DirectMessage> getDirectMessages(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<DirectMessage> getSentDirectMessages() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<DirectMessage> getSentDirectMessages(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectMessage sendDirectMessage(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectMessage sendDirectMessage(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectMessage showDirectMessage(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createFriendship(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createFriendship(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createFriendship(long arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createFriendship(String arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyFriendship(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyFriendship(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFollowersIDs(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFollowersIDs(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFollowersIDs(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFollowersIDs(long arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFollowersIDs(String arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(long arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(String arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(long arg0, long arg1, int arg2, boolean arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(String arg0, long arg1, int arg2, boolean arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFriendsIDs(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFriendsIDs(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFriendsIDs(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFriendsIDs(long arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getFriendsIDs(String arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(long arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(String arg0, long arg1, int arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(long arg0, long arg1, int arg2, boolean arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(String arg0, long arg1, int arg2, boolean arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getIncomingFriendships(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getNoRetweetsFriendships() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getOutgoingFriendships(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Friendship> lookupFriendships(long... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Friendship> lookupFriendships(String... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Relationship showFriendship(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Relationship showFriendship(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Relationship updateFriendship(long arg0, boolean arg1, boolean arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Relationship updateFriendship(String arg0, boolean arg1, boolean arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createBlock(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createBlock(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createMute(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createMute(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyBlock(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyBlock(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyMute(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User destroyMute(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountSettings getAccountSettings() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getBlocksIDs() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getBlocksIDs(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getBlocksList() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getBlocksList(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getContributees(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getContributees(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getContributors(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getContributors(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDs getMutesIDs(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getMutesList(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> lookupUsers(long... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> lookupUsers(String... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeProfileBanner() throws TwitterException {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseList<User> searchUsers(String arg0, int arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUser(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUser(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccountSettings updateAccountSettings(Integer arg0, Boolean arg1, String arg2, String arg3, String arg4,
			String arg5) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateProfile(String arg0, String arg1, String arg2, String arg3) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateProfileBackgroundImage(File arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateProfileBackgroundImage(InputStream arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProfileBanner(File arg0) throws TwitterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProfileBanner(InputStream arg0) throws TwitterException {
		// TODO Auto-generated method stub

	}

	@Override
	public User updateProfileColors(String arg0, String arg1, String arg2, String arg3, String arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateProfileImage(File arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateProfileImage(InputStream arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User verifyCredentials() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getMemberSuggestions(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Category> getSuggestedUserCategories() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<User> getUserSuggestions(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status createFavorite(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status destroyFavorite(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites(Paging arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites(long arg0, Paging arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getFavorites(String arg0, Paging arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserList(String arg0, boolean arg1, String arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMember(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMember(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMember(String arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(long arg0, long... arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(long arg0, String... arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(long arg0, String arg1, long... arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(String arg0, String arg1, long... arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(long arg0, String arg1, String... arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListMembers(String arg0, String arg1, String... arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListSubscription(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListSubscription(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList createUserListSubscription(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserList(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserList(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserList(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMember(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMember(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMember(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMember(String arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMembers(long arg0, String[] arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMembers(long arg0, long[] arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListMembers(String arg0, String arg1, String[] arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListSubscription(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListSubscription(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList destroyUserListSubscription(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, int arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(String arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, int arg1, long arg2, boolean arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, String arg1, int arg2, long arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(String arg0, String arg1, int arg2, long arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(long arg0, String arg1, int arg2, long arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(String arg0, String arg1, int arg2, long arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(int arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(long arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(String arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(String arg0, long arg1, boolean arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(long arg0, long arg1, boolean arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(String arg0, int arg1, long arg2, boolean arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListMemberships(long arg0, int arg1, long arg2, boolean arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserListStatuses(long arg0, Paging arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserListStatuses(long arg0, String arg1, Paging arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Status> getUserListStatuses(String arg0, String arg1, Paging arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, int arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(String arg0, String arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, int arg1, long arg2, boolean arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, String arg1, int arg2, long arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(String arg0, String arg1, int arg2, long arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(long arg0, String arg1, int arg2, long arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(String arg0, String arg1, int arg2, long arg3, boolean arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListSubscriptions(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListSubscriptions(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListSubscriptions(String arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListSubscriptions(long arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<UserList> getUserLists(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<UserList> getUserLists(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<UserList> getUserLists(String arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<UserList> getUserLists(long arg0, boolean arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListsOwnerships(String arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListsOwnerships(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListsOwnerships(String arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PagableResponseList<UserList> getUserListsOwnerships(long arg0, int arg1, long arg2)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList showUserList(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList showUserList(long arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList showUserList(String arg0, String arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListMembership(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListMembership(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListMembership(String arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListSubscription(long arg0, long arg1) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListSubscription(long arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUserListSubscription(String arg0, String arg1, long arg2) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList updateUserList(long arg0, String arg1, boolean arg2, String arg3) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList updateUserList(long arg0, String arg1, String arg2, boolean arg3, String arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserList updateUserList(String arg0, String arg1, String arg2, boolean arg3, String arg4)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SavedSearch createSavedSearch(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SavedSearch destroySavedSearch(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SavedSearch showSavedSearch(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Place getGeoDetails(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Place> getSimilarPlaces(GeoLocation arg0, String arg1, String arg2, String arg3)
			throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Place> reverseGeoCode(GeoQuery arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Place> searchPlaces(GeoQuery arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Location> getAvailableTrends() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Location> getClosestTrends(GeoLocation arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trends getPlaceTrends(int arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User reportSpam(long arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User reportSpam(String arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TwitterAPIConfiguration getAPIConfiguration() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseList<Language> getLanguages() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrivacyPolicy() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, RateLimitStatus> getRateLimitStatus() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, RateLimitStatus> getRateLimitStatus(String... arg0) throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTermsOfService() throws TwitterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectMessagesResources directMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FavoritesResources favorites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FriendsFollowersResources friendsFollowers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HelpResources help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListsResources list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlacesGeoResources placesGeo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SavedSearchesResources savedSearches() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResource search() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpamReportingResource spamReporting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SuggestedUsersResources suggestedUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimelinesResources timelines() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrendsResources trends() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TweetsResources tweets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UsersResources users() {
		// TODO Auto-generated method stub
		return null;
	}

}

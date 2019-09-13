package com.webservicemaster.iirs.service.friendship;

import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.webservicemaster.iirs.domain.master.Friendship;

public interface FriendshipService {

	public JSONObject inviteFriendViaQRCode( Friendship data );	
	public JSONObject inviteJustFriend( Friendship data );	
	public JSONObject confirmFriendship( short status, short confirmFriendship, long friendId ) throws ParseException;
	public boolean checkFriendshipStatus( long invitatorId, long invitedId );
	public boolean checkFriendShipForConfirm( long friendId, long invitedId );
	public boolean checkIsInvitedWithSomeone( long invitedId );
	public JSONObject getFriendList( long customerId, int start, int limit ) throws Exception;
	public JSONObject checkPhoneNumberIsMember( long customerId, JSONArray phoneNumber ) throws Exception;
	public boolean isFriendRequestFutureBlock( long invitatorId, long invitedId );
	public long inviteByCompany( long customerId ) throws ParseException;
	public Friendship save( Friendship data );
	public Friendship findByFriendId( long friendId );
}

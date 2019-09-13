package com.webservicemaster.iirs.service.friendship;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.webservicemaster.iirs.dao.master.FriendshipRepository;
import com.webservicemaster.iirs.domain.master.Customer;
import com.webservicemaster.iirs.domain.master.Friendship;
import com.webservicemaster.iirs.domain.master.ViewFriendList;
import com.webservicemaster.iirs.service.customer.CustomerService;
import com.webservicemaster.iirs.utility.AES;
import com.webservicemaster.iirs.utility.Utility;

@Service
public class FriendshipServiceImpl implements FriendshipService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FriendshipServiceImpl.class);
	private FriendshipRepository friendRepo;
	
	@Value("${db.key}")
	private String dbKey;
	
	@Autowired
	private AES aes;
	
	@PersistenceContext(unitName="masterPU")
	private EntityManager manager;
	
	@Autowired
	FriendshipServiceImpl(FriendshipRepository friendRepo) {
        this.friendRepo = friendRepo;
    }
	
	@Autowired
	private Utility util;
	
	@Autowired
	private CustomerService customerService;
	
	@Value("${customer.photo.url}")
	private String customerPhotoURL;
	
	@Transactional
	@Override
	public JSONObject inviteFriendViaQRCode( Friendship data ){
		
		JSONObject jo		= new JSONObject();
		
		friendRepo.save(data);
		
		if( data.getFriendId() > 0 ){
			
			jo.put("errCode", "00");
			jo.put("errMsg", "You've successfully added friend.");
			
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Add friend failed.");
			
		}
		
		return jo;
		
	}
	
	@Transactional
	@Override
	public JSONObject inviteJustFriend( Friendship data ){
		
		JSONObject jo		= new JSONObject();
		
		friendRepo.save(data);
		
		if( data.getFriendId() > 0 ){
			
			jo.put("errCode", "00");
			jo.put("errMsg", "You've successfully added friend.");
			
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Add friend failed.");
			
		}
		
		return jo;
		
	}
	
	@Transactional
	@Override
	public JSONObject confirmFriendship( short status, short confirmFriendship, long friendId ) throws ParseException{
		
		JSONObject jo		= new JSONObject();
		
		int updateStatus 	= friendRepo.confirmFriendship(status, confirmFriendship, util.getDateTime(), friendId);
		
		if( updateStatus > 0 ){
			
			jo.put("errCode", "00");
			jo.put("errMsg", "You've successfully confirm friendship.");
			
		}else{
			
			jo.put("errCode", "-99");
			jo.put("errMsg", "Confirm friendship failed.");
			
		}
		
		return jo;
		
	}
	
	
	@Override
	public boolean checkFriendshipStatus( long invitatorId, long invitedId ){
		
		boolean flag 			= true;
		
		List lFriendship		= friendRepo.checkFriendshipStatus(invitatorId, invitedId);
		
		if( Integer.parseInt( lFriendship.get(0).toString() ) > 0 ){
			
			flag 				= false;
			
		}
		
		return flag;
		
	}
	
	@Override
	public boolean isFriendRequestFutureBlock( long invitatorId, long invitedId ){
		
		boolean flag 			= true;
		
		List lBlock 			= friendRepo.checkFriendRequestFutureBlock(invitatorId, invitedId);
		
		if( Integer.parseInt( lBlock.get(0).toString() ) == 0 ){
			flag 				= false;
		}
		
		return flag;
		
	}
	
	@Override
	public boolean checkFriendShipForConfirm( long friendId,
											  long invitedId ){
		
		boolean flag 			= false;
		
		List lFriendship		= friendRepo.checkFriendShipForConfirm(friendId, invitedId);
		
		if( Integer.parseInt( lFriendship.get(0).toString() ) > 0 ){
			
			flag 				= true;
			
		}
		
		return flag;
		
	}
	
	@Override
	public boolean checkIsInvitedWithSomeone( long invitedId ){
		
		boolean flag 			= false;
		List lFriendship		= friendRepo.checkIsInvitedWithSomeone(invitedId);
		
		if( Integer.parseInt( lFriendship.get(0).toString() ) > 0 ){
			
			flag 				= true;
			
		}
		
		return flag;
		
	}
	
	@Override
	public JSONObject getFriendList( long customerId,
									 int start,
									 int limit) throws Exception{
		
		JSONObject joResult 			= new JSONObject();
		JSONArray jaData 				= new JSONArray();
		JSONObject joData 				= null;
		String photoURL 				= "";
		short invitator 				= 0;
		
		//List<Object[]> lFriendList		= friendRepo.getFriendList(customerId, start, limit);
		List<ViewFriendList> lFriendList 	= manager.createQuery(" FROM ViewFriendList v WHERE v.userId = :id AND v.type <> 'special_just_friend' ")
											.setParameter("id", customerId)
											.setFirstResult(start)
											.setMaxResults(limit)
											.getResultList();
		
		joResult.put("errCode", "00");
		
		for( ViewFriendList vl : lFriendList ){
			
			joData 						= new JSONObject();
			invitator 					= 0;
			
			if( vl.getCustomerPhoto() != null || !vl.getCustomerPhoto().equals("") ){
				photoURL 				= customerPhotoURL + vl.getCustomerPhoto();
			}else{
				photoURL 				= "";
			}
			
			joData.put("friendId", vl.getCustomerId());
			joData.put("friendName", aes.decrypt(vl.getCustomerName(), dbKey));
			joData.put("photo", photoURL);
			joData.put("isInvitator", vl.getIsInvitator());
			jaData.add(joData);
			
		}
		
		joResult.put("data", jaData);
		
		return joResult;
		
	}
	
	public JSONObject checkPhoneNumberIsMember( long customerId,
												JSONArray arrPhoneNumber ) throws Exception{
		
		JSONObject joResult 		= new JSONObject();
		JSONObject joData 			= new JSONObject();
		JSONArray jaArray 			= new JSONArray();
		
		String phoneNumber 			= "";
		
		if( arrPhoneNumber.size() > 0 ){
			
			for( int i = 0; i < arrPhoneNumber.size(); i++ ){
				
				joData 				= new JSONObject();
				
				phoneNumber 		= arrPhoneNumber.get(i).toString();
				
				/*1. Check is Member or Not*/
				Customer c			= customerService.findByCustomerPhoneNumber( AES.encrypt( phoneNumber,dbKey ) );
				if( c != null ){
					
					/*2. Check friendship status*/
					List lStatus 	= friendRepo.checkFriendshipStatus(customerId, c.getCustomerId());
					
					if( Integer.parseInt( lStatus.get(0).toString() ) > 0 ){
						
						joData.put("phoneNumber", phoneNumber);
						joData.put("isMember", 1);
						joData.put("friendshipStatus", 1);
						
					}else{
						
						joData.put("phoneNumber", phoneNumber);
						joData.put("isMember", 1);
						joData.put("friendshipStatus", 0);
						
					}
					
				}else{
					
					joData.put("phoneNumber", phoneNumber);
					joData.put("isMember", 0);
					joData.put("friendshipStatus", 0);
					
				}
				
				jaArray.add(joData);
				
			}
			
			joResult.put("errCode", "00");
			joResult.put("errMsg", "OK");
			joResult.put("data", jaArray);
			
		}else{
			
			joResult.put("errCode", "-99");
			joResult.put("errMsg", "Parameter not valid");
			
		}
		
		return joResult;
		
	}
	
	
	@Override
	public long inviteByCompany( long customerId ) throws ParseException{
		
		long friendId 		=  0;
		
		Friendship f 		= new Friendship();
		f.setInvitationMethod((short)5);
		f.setInvitatorId(-2);
		f.setInviteDateTime(util.getDateTime());
		f.setInvitedId(customerId);
		f.setStatus((short)1);
		f.setType("invited");
		friendRepo.save(f);
		
		friendId 			= f.getFriendId();
		
		return friendId;
		
	}
	
	public Friendship save( Friendship data ){
		return friendRepo.save(data);
	}
	
	@Override
	public Friendship findByFriendId( long friendId ){
		return friendRepo.findByFriendId(friendId);
	}
	
}

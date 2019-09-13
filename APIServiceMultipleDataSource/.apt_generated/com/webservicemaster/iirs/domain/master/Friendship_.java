package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Friendship.class)
public abstract class Friendship_ {

	public static volatile SingularAttribute<Friendship, Short> isBlockFutureInvitation;
	public static volatile SingularAttribute<Friendship, Long> friendId;
	public static volatile SingularAttribute<Friendship, Date> inviteDateTime;
	public static volatile SingularAttribute<Friendship, Short> invitationMethod;
	public static volatile SingularAttribute<Friendship, Long> invitedId;
	public static volatile SingularAttribute<Friendship, Date> confirmDateTime;
	public static volatile SingularAttribute<Friendship, String> type;
	public static volatile SingularAttribute<Friendship, Long> invitatorId;
	public static volatile SingularAttribute<Friendship, Short> status;

}


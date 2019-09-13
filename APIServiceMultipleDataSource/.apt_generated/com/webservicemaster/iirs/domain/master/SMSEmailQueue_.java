package com.webservicemaster.iirs.domain.master;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SMSEmailQueue.class)
public abstract class SMSEmailQueue_ {

	public static volatile SingularAttribute<SMSEmailQueue, String> subject;
	public static volatile SingularAttribute<SMSEmailQueue, Long> id;
	public static volatile SingularAttribute<SMSEmailQueue, String> type;
	public static volatile SingularAttribute<SMSEmailQueue, String> body;
	public static volatile SingularAttribute<SMSEmailQueue, String> destinationAccount;
	public static volatile SingularAttribute<SMSEmailQueue, String> createDate;

}


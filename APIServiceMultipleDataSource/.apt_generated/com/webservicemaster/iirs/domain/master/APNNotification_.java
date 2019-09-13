package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(APNNotification.class)
public abstract class APNNotification_ {

	public static volatile SingularAttribute<APNNotification, Short> statusResponse;
	public static volatile SingularAttribute<APNNotification, String> data;
	public static volatile SingularAttribute<APNNotification, Long> customerId;
	public static volatile SingularAttribute<APNNotification, String> icon;
	public static volatile SingularAttribute<APNNotification, Date> postDate;
	public static volatile SingularAttribute<APNNotification, Long> ayaresNotificationId;
	public static volatile SingularAttribute<APNNotification, String> title;
	public static volatile SingularAttribute<APNNotification, String> message;
	public static volatile SingularAttribute<APNNotification, String> responseMessage;
	public static volatile SingularAttribute<APNNotification, Long> apnNotificationId;
	public static volatile SingularAttribute<APNNotification, Short> statusSend;
	public static volatile SingularAttribute<APNNotification, String> apnToken;

}


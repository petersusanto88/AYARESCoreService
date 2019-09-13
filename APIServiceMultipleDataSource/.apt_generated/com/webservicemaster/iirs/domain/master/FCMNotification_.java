package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FCMNotification.class)
public abstract class FCMNotification_ {

	public static volatile SingularAttribute<FCMNotification, Short> statusResponse;
	public static volatile SingularAttribute<FCMNotification, String> data;
	public static volatile SingularAttribute<FCMNotification, Long> customerId;
	public static volatile SingularAttribute<FCMNotification, String> icon;
	public static volatile SingularAttribute<FCMNotification, Date> postDate;
	public static volatile SingularAttribute<FCMNotification, Long> notificationId;
	public static volatile SingularAttribute<FCMNotification, Long> ayaresNotificationId;
	public static volatile SingularAttribute<FCMNotification, String> title;
	public static volatile SingularAttribute<FCMNotification, String> message;
	public static volatile SingularAttribute<FCMNotification, String> responseMessage;
	public static volatile SingularAttribute<FCMNotification, String> fcmToken;
	public static volatile SingularAttribute<FCMNotification, Short> statusSend;

}


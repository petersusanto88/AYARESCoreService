package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notification.class)
public abstract class Notification_ {

	public static volatile SingularAttribute<Notification, String> data;
	public static volatile SingularAttribute<Notification, String> subject;
	public static volatile SingularAttribute<Notification, Long> customerId;
	public static volatile SingularAttribute<Notification, String> icon;
	public static volatile SingularAttribute<Notification, Short> hasRead;
	public static volatile SingularAttribute<Notification, Long> notificationId;
	public static volatile SingularAttribute<Notification, String> notificationType;
	public static volatile SingularAttribute<Notification, String> body;
	public static volatile SingularAttribute<Notification, Date> readAt;
	public static volatile SingularAttribute<Notification, Date> notificationDate;

}


package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Advertise.class)
public abstract class Advertise_ {

	public static volatile SingularAttribute<Advertise, Short> sequence;
	public static volatile SingularAttribute<Advertise, String> file;
	public static volatile SingularAttribute<Advertise, Date> endDate;
	public static volatile SingularAttribute<Advertise, Short> isDelete;
	public static volatile SingularAttribute<Advertise, Long> advertiseId;
	public static volatile SingularAttribute<Advertise, Date> startDate;
	public static volatile SingularAttribute<Advertise, Short> status;

}


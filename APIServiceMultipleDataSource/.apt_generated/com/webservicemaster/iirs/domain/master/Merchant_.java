package com.webservicemaster.iirs.domain.master;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Merchant.class)
public abstract class Merchant_ {

	public static volatile SingularAttribute<Merchant, Short> merchantRebateType;
	public static volatile SingularAttribute<Merchant, Long> accountId;
	public static volatile SingularAttribute<Merchant, Double> merchantRebateValue;
	public static volatile SingularAttribute<Merchant, Long> merchantId;
	public static volatile SingularAttribute<Merchant, String> logo;
	public static volatile SingularAttribute<Merchant, String> merchantName;

}


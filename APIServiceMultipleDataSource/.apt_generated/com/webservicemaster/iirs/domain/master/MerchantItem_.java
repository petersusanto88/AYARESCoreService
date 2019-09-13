package com.webservicemaster.iirs.domain.master;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MerchantItem.class)
public abstract class MerchantItem_ {

	public static volatile SingularAttribute<MerchantItem, Long> itemId;
	public static volatile SingularAttribute<MerchantItem, Integer> merchantItemCategoryId;
	public static volatile SingularAttribute<MerchantItem, String> itemName;
	public static volatile SingularAttribute<MerchantItem, String> expired;
	public static volatile SingularAttribute<MerchantItem, Long> merchantId;
	public static volatile SingularAttribute<MerchantItem, Float> rebate;
	public static volatile SingularAttribute<MerchantItem, Double> price;
	public static volatile SingularAttribute<MerchantItem, String> itemCode;

}


package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EVoucherProduct.class)
public abstract class EVoucherProduct_ {

	public static volatile SingularAttribute<EVoucherProduct, String> validationPrefixUsername;
	public static volatile SingularAttribute<EVoucherProduct, Short> isAutoCampaignAfterRegister;
	public static volatile SingularAttribute<EVoucherProduct, Long> merchantId;
	public static volatile SingularAttribute<EVoucherProduct, Date> endDate;
	public static volatile SingularAttribute<EVoucherProduct, Long> voucherId;
	public static volatile SingularAttribute<EVoucherProduct, Double> voucherValue;
	public static volatile SingularAttribute<EVoucherProduct, Short> isDelete;
	public static volatile SingularAttribute<EVoucherProduct, String> photo;
	public static volatile SingularAttribute<EVoucherProduct, String> productName;
	public static volatile SingularAttribute<EVoucherProduct, Date> startDate;
	public static volatile SingularAttribute<EVoucherProduct, Short> status;

}


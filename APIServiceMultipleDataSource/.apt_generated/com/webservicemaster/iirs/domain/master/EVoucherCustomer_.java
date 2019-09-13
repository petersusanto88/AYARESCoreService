package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EVoucherCustomer.class)
public abstract class EVoucherCustomer_ {

	public static volatile SingularAttribute<EVoucherCustomer, Long> voucherCustomerId;
	public static volatile SingularAttribute<EVoucherCustomer, Date> endDate;
	public static volatile SingularAttribute<EVoucherCustomer, Long> voucherId;
	public static volatile SingularAttribute<EVoucherCustomer, Short> isDelete;
	public static volatile SingularAttribute<EVoucherCustomer, Long> customerId;
	public static volatile SingularAttribute<EVoucherCustomer, String> voucherCode;
	public static volatile SingularAttribute<EVoucherCustomer, Date> startDate;
	public static volatile SingularAttribute<EVoucherCustomer, Short> status;

}


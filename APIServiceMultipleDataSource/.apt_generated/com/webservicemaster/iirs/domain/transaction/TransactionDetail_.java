package com.webservicemaster.iirs.domain.transaction;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransactionDetail.class)
public abstract class TransactionDetail_ {

	public static volatile SingularAttribute<TransactionDetail, Double> total;
	public static volatile SingularAttribute<TransactionDetail, Long> transactionCodeId;
	public static volatile SingularAttribute<TransactionDetail, String> transType;
	public static volatile SingularAttribute<TransactionDetail, Long> transId;
	public static volatile SingularAttribute<TransactionDetail, Long> transDetailId;
	public static volatile SingularAttribute<TransactionDetail, String> accountNumberDestination;
	public static volatile SingularAttribute<TransactionDetail, String> transactionName;
	public static volatile SingularAttribute<TransactionDetail, Long> accountIdDestination;
	public static volatile SingularAttribute<TransactionDetail, Date> transactionDate;

}


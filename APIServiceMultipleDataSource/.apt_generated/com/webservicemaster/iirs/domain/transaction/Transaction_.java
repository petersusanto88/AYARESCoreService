package com.webservicemaster.iirs.domain.transaction;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, Long> branchId;
	public static volatile SingularAttribute<Transaction, String> transactionNumber;
	public static volatile SingularAttribute<Transaction, String> branchName;
	public static volatile SingularAttribute<Transaction, String> accountNumber;
	public static volatile SingularAttribute<Transaction, Long> transactionId;
	public static volatile SingularAttribute<Transaction, String> merchantName;
	public static volatile SingularAttribute<Transaction, Short> rebateType;
	public static volatile SingularAttribute<Transaction, Short> transactionType;
	public static volatile SingularAttribute<Transaction, String> merchantIcon40x60;
	public static volatile SingularAttribute<Transaction, Long> accountId;
	public static volatile SingularAttribute<Transaction, Long> merchantId;
	public static volatile SingularAttribute<Transaction, Long> voucherId;
	public static volatile SingularAttribute<Transaction, String> transDate;
	public static volatile SingularAttribute<Transaction, String> merchantTransactionNumber;
	public static volatile SingularAttribute<Transaction, Long> totalPurchase;
	public static volatile SingularAttribute<Transaction, Long> totalRebate;
	public static volatile SingularAttribute<Transaction, Short> status;

}


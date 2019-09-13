package com.webservicemaster.data.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransactionLog.class)
public abstract class TransactionLog_ {

	public static volatile SingularAttribute<TransactionLog, Integer> total;
	public static volatile SingularAttribute<TransactionLog, Integer> transactionCodeId;
	public static volatile SingularAttribute<TransactionLog, Integer> senderPrevSaldo;
	public static volatile SingularAttribute<TransactionLog, Integer> logId;
	public static volatile SingularAttribute<TransactionLog, String> postDate;
	public static volatile SingularAttribute<TransactionLog, Integer> terminalId;
	public static volatile SingularAttribute<TransactionLog, String> accountNumber;
	public static volatile SingularAttribute<TransactionLog, Integer> recipientPrevSaldo;
	public static volatile SingularAttribute<TransactionLog, String> branchId;
	public static volatile SingularAttribute<TransactionLog, Long> userId;
	public static volatile SingularAttribute<TransactionLog, String> userName;
	public static volatile SingularAttribute<TransactionLog, Integer> merchantId;
	public static volatile SingularAttribute<TransactionLog, Integer> costId;
	public static volatile SingularAttribute<TransactionLog, Short> status;
	public static volatile SingularAttribute<TransactionLog, Integer> productCode;
	public static volatile SingularAttribute<TransactionLog, String> extra1;
	public static volatile SingularAttribute<TransactionLog, Integer> senderId;
	public static volatile SingularAttribute<TransactionLog, String> extra2;
	public static volatile SingularAttribute<TransactionLog, String> extra3;
	public static volatile SingularAttribute<TransactionLog, String> transactionDate;
	public static volatile SingularAttribute<TransactionLog, Short> statusSync;
	public static volatile SingularAttribute<TransactionLog, Integer> issuerId;
	public static volatile SingularAttribute<TransactionLog, Long> recipientId;
	public static volatile SingularAttribute<TransactionLog, Integer> issuerBankCode;
	public static volatile SingularAttribute<TransactionLog, Integer> outletId;
	public static volatile SingularAttribute<TransactionLog, String> statApp;

}


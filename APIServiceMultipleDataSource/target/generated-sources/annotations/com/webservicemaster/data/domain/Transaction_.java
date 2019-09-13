package com.webservicemaster.data.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, Double> total;
	public static volatile SingularAttribute<Transaction, Long> transactionId;
	public static volatile SingularAttribute<Transaction, Short> transactionCode;
	public static volatile SingularAttribute<Transaction, String> customerAccountNumber;
	public static volatile SingularAttribute<Transaction, Double> customerCreditFee;
	public static volatile SingularAttribute<Transaction, String> customerSenderAccountNumber;
	public static volatile SingularAttribute<Transaction, Integer> senderIssuerId;
	public static volatile SingularAttribute<Transaction, Long> customerId;
	public static volatile SingularAttribute<Transaction, Double> customerDebetFee;
	public static volatile SingularAttribute<Transaction, Integer> recipientIssuerId;
	public static volatile SingularAttribute<Transaction, Long> customerRecipientId;
	public static volatile SingularAttribute<Transaction, Short> status;
	public static volatile SingularAttribute<Transaction, Long> claimedLogId;
	public static volatile SingularAttribute<Transaction, String> transactionFrom;
	public static volatile SingularAttribute<Transaction, String> transactionName;
	public static volatile SingularAttribute<Transaction, String> branchCode;
	public static volatile SingularAttribute<Transaction, String> transactionDate;
	public static volatile SingularAttribute<Transaction, String> customerRecipientAccountNumber;
	public static volatile SingularAttribute<Transaction, Short> hasClaimed;
	public static volatile SingularAttribute<Transaction, Long> issuerId;
	public static volatile SingularAttribute<Transaction, Double> customerCurrentBalance;
	public static volatile SingularAttribute<Transaction, Integer> adminId;
	public static volatile SingularAttribute<Transaction, Double> customerPreviousBalance;
	public static volatile SingularAttribute<Transaction, String> transactionAccountNumber;
	public static volatile SingularAttribute<Transaction, Long> customerSenderId;
	public static volatile SingularAttribute<Transaction, String> transactionNumber;
	public static volatile SingularAttribute<Transaction, Short> transUserType;

}


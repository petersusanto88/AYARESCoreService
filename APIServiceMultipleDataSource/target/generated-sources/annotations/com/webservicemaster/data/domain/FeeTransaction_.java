package com.webservicemaster.data.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FeeTransaction.class)
public abstract class FeeTransaction_ {

	public static volatile SingularAttribute<FeeTransaction, Integer> transactionCodeId;
	public static volatile SingularAttribute<FeeTransaction, Short> senderCreditType;
	public static volatile SingularAttribute<FeeTransaction, Short> senderCreditMDR;
	public static volatile SingularAttribute<FeeTransaction, Double> senderCreditFlatFee;
	public static volatile SingularAttribute<FeeTransaction, Integer> feeId;
	public static volatile SingularAttribute<FeeTransaction, Integer> productCode;
	public static volatile SingularAttribute<FeeTransaction, Short> senderDebetType;
	public static volatile SingularAttribute<FeeTransaction, Short> senderDebetMDR;
	public static volatile SingularAttribute<FeeTransaction, Double> senderDebetFlatFee;

}


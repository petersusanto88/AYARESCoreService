package com.webservicemaster.iirs.domain.transaction;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Withdrawl.class)
public abstract class Withdrawl_ {

	public static volatile SingularAttribute<Withdrawl, Double> amount;
	public static volatile SingularAttribute<Withdrawl, String> accountName;
	public static volatile SingularAttribute<Withdrawl, String> transferFromAccountName;
	public static volatile SingularAttribute<Withdrawl, Double> fee;
	public static volatile SingularAttribute<Withdrawl, String> transferFromAccountNumber;
	public static volatile SingularAttribute<Withdrawl, Long> withdrawlId;
	public static volatile SingularAttribute<Withdrawl, String> accountNumber;
	public static volatile SingularAttribute<Withdrawl, String> transferDate;
	public static volatile SingularAttribute<Withdrawl, Short> statusTransfer;
	public static volatile SingularAttribute<Withdrawl, String> transferFromBankName;
	public static volatile SingularAttribute<Withdrawl, String> transferToBankName;
	public static volatile SingularAttribute<Withdrawl, Long> accountId;
	public static volatile SingularAttribute<Withdrawl, String> transferToBankCode;
	public static volatile SingularAttribute<Withdrawl, String> postDate;
	public static volatile SingularAttribute<Withdrawl, String> transferToAccountNumber;
	public static volatile SingularAttribute<Withdrawl, String> transferToAccountName;

}


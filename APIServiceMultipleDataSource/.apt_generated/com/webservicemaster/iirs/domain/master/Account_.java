package com.webservicemaster.iirs.domain.master;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ {

	public static volatile SingularAttribute<Account, Long> accountId;
	public static volatile SingularAttribute<Account, Double> balance;
	public static volatile SingularAttribute<Account, String> settlementBankCode;
	public static volatile SingularAttribute<Account, String> accountCategory;
	public static volatile SingularAttribute<Account, Short> accountType;
	public static volatile SingularAttribute<Account, String> accountNumber;
	public static volatile SingularAttribute<Account, Long> memberId;
	public static volatile SingularAttribute<Account, Short> status;

}


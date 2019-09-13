package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MerchantUserAccess.class)
public abstract class MerchantUserAccess_ {

	public static volatile SingularAttribute<MerchantUserAccess, String> userLogin;
	public static volatile SingularAttribute<MerchantUserAccess, Long> branchId;
	public static volatile SingularAttribute<MerchantUserAccess, String> userPassword;
	public static volatile SingularAttribute<MerchantUserAccess, Long> merchantId;
	public static volatile SingularAttribute<MerchantUserAccess, Integer> roleId;
	public static volatile SingularAttribute<MerchantUserAccess, Date> tokenExpired;
	public static volatile SingularAttribute<MerchantUserAccess, String> sessionToken;
	public static volatile SingularAttribute<MerchantUserAccess, String> userName;
	public static volatile SingularAttribute<MerchantUserAccess, Integer> userId;
	public static volatile SingularAttribute<MerchantUserAccess, Short> status;

}


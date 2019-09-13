package com.webservicemaster.iirs.domain.master;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MerchantBranches.class)
public abstract class MerchantBranches_ {

	public static volatile SingularAttribute<MerchantBranches, Integer> branchId;
	public static volatile SingularAttribute<MerchantBranches, String> address;
	public static volatile SingularAttribute<MerchantBranches, Long> merchantId;
	public static volatile SingularAttribute<MerchantBranches, Float> latitude;
	public static volatile SingularAttribute<MerchantBranches, String> branchName;
	public static volatile SingularAttribute<MerchantBranches, Float> longitude;

}


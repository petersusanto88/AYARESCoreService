package com.webservicemaster.iirs.domain.master;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IncentiveSetting.class)
public abstract class IncentiveSetting_ {

	public static volatile SingularAttribute<IncentiveSetting, Integer> incentiveSettingId;
	public static volatile SingularAttribute<IncentiveSetting, Date> endDate;
	public static volatile SingularAttribute<IncentiveSetting, String> incentiveType;
	public static volatile SingularAttribute<IncentiveSetting, Date> startDate;
	public static volatile SingularAttribute<IncentiveSetting, String> settingName;
	public static volatile SingularAttribute<IncentiveSetting, Short> status;

}


package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

abstract public class PlanProcessor extends BaseEntity {
    abstract void process();
}

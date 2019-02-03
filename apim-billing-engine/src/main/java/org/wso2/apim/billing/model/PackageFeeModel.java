package org.wso2.apim.billing.model;

import org.wso2.apim.billing.domain.BillingAttribute;

import java.io.Serializable;
import java.util.List;

public class PackageFeeModel implements Serializable {
    protected long totalCost;
    protected List<BillingAttribute> attributes;

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public List<BillingAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BillingAttribute> attributes) {
        this.attributes = attributes;
    }
}

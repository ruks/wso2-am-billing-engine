package org.wso2.apim.billing.model;

import org.wso2.apim.billing.domain.BillingAttribute;

import java.io.Serializable;
import java.util.List;

public class PackageFeeModel implements Serializable {
    protected double totalCost;
    protected List<BillingAttribute> attributes;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<BillingAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BillingAttribute> attributes) {
        this.attributes = attributes;
    }
}

package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "billing_attribute")
public class BillingAttribute extends BaseEntity {
    @Column(name = "idx")
    private int index;
    @Column(name = "name")
    private String key;
    private String value;
    private String displayName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_package_id")
    private BillingModel billingModel;

    public BillingAttribute() {
    }

    public BillingAttribute(int index, String key, String displayName, String value) {
        this.index = index;
        this.key = key;
        this.value = value;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BillingModel getBillingModel() {
        return billingModel;
    }

    public void setBillingModel(BillingModel billingModel) {
        this.billingModel = billingModel;
    }
}

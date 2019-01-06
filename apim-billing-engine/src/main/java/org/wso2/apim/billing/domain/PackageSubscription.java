package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "package_subscription")
public class PackageSubscription extends BaseEntity {
    private String user;
    private String packageID;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }
}

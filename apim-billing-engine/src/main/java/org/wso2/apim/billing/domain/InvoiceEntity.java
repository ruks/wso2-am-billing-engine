/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.wso2.apim.billing.domain;

import org.wso2.apim.billing.commons.domain.BaseEntity;
import org.wso2.apim.billing.model.PackageFeeModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "invoice")
public class InvoiceEntity extends BaseEntity {

    private String userID;
    private String createdDate;
    private String dueDate;
    private String billedMonth;
    @Column(columnDefinition = "TEXT")
    private String invoiceJson;
    private long totalCost;

    @Transient
    private UserInfo userInfo;
    @Transient
    private List<PackageFeeModel> packageFeeModels;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getInvoiceJson() {
        return invoiceJson;
    }

    public void setInvoiceJson(String invoiceJson) {
        this.invoiceJson = invoiceJson;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<PackageFeeModel> getPackageFeeModels() {
        return packageFeeModels;
    }

    public void setPackageFeeModels(List<PackageFeeModel> packageFeeModels) {
        this.packageFeeModels = packageFeeModels;
    }

    public String getBilledMonth() {
        return billedMonth;
    }

    public void setBilledMonth(String billedMonth) {
        this.billedMonth = billedMonth;
    }
}

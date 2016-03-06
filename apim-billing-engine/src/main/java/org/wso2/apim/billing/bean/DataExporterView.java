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
package org.wso2.apim.billing.bean;

import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.services.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.io.Serializable;
import java.util.List;

@ManagedBean
public class DataExporterView implements Serializable {

    private List<InvoiceEntity> invoices;
    private InvoiceEntity selectedId;

    @ManagedProperty("#{invoiceService}") private InvoiceService invoiceService;

    @PostConstruct
    public void init() {
        invoices = invoiceService.listInvoices(null);
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public InvoiceEntity getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(InvoiceEntity selectedId) {
        this.selectedId = selectedId;
    }

    public List<InvoiceEntity> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceEntity> invoices) {
        this.invoices = invoices;
    }
}

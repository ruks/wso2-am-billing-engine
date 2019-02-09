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
package org.wso2.apim.billing.services.impl;

import com.google.gson.Gson;
import org.wso2.apim.billing.dao.UsagePlanDao;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.domain.UserEntity;
import org.wso2.apim.billing.domain.UserInfo;
import org.wso2.apim.billing.model.PackageFeeModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InvoiceGenerator {
    private UsagePlanDao usagePlanDao;
    private MeteredPlanProcessor meteredPlanProcessor;
    private QuotaPlanProcessor quotaPlanProcessor;

    public InvoiceGenerator() {

    }

    public UsagePlanDao getUsagePlanDao() {
        return usagePlanDao;
    }

    public void setUsagePlanDao(UsagePlanDao usagePlanDao) {
        this.usagePlanDao = usagePlanDao;
    }

    public MeteredPlanProcessor getMeteredPlanProcessor() {
        return meteredPlanProcessor;
    }

    public void setMeteredPlanProcessor(MeteredPlanProcessor meteredPlanProcessor) {
        this.meteredPlanProcessor = meteredPlanProcessor;
    }

    public QuotaPlanProcessor getQuotaPlanProcessor() {
        return quotaPlanProcessor;
    }

    public void setQuotaPlanProcessor(QuotaPlanProcessor quotaPlanProcessor) {
        this.quotaPlanProcessor = quotaPlanProcessor;
    }

    public InvoiceEntity process(UserEntity user, String selectedSubscriber, int selectedMonth) {
        List<PackageFeeModel> packageFeeModels = new ArrayList<>();
        List<BillingModel> packages = usagePlanDao.loadBillingPlansOfUser(selectedSubscriber);
        for (BillingModel aPackage : packages) {
            PackageFeeModel model = null;
            if ("metered".equals(aPackage.getPackageType())) {
                model = meteredPlanProcessor.process(user.getUserName(), aPackage, selectedMonth);
            } else if ("quota".equals(aPackage.getPackageType())) {
                model = quotaPlanProcessor.process(user.getUserName(), aPackage, selectedMonth);
            }
            if (model != null) {
                packageFeeModels.add(model);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        String billDate = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        String dueDate = dateFormat.format(calendar.getTime());

        calendar.set(Calendar.MONTH, selectedMonth);
        DateFormat dateFormat2 = new SimpleDateFormat("MMMM yyyy");
        String billedMonth = dateFormat2.format(calendar.getTime());

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setUserID(user.getUserName());
        invoiceEntity.setCreatedDate(billDate);
        invoiceEntity.setDueDate(dueDate);
        invoiceEntity.setBilledMonth(billedMonth);

        long totalCost = 0;
        for (PackageFeeModel model : packageFeeModels) {
            totalCost+=model.getTotalCost();
        }
        invoiceEntity.setTotalCost(totalCost);
        invoiceEntity.setPackageFeeModels(packageFeeModels);
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setCompany(user.getCompany());
        userInfo.setEmail(user.getEmail());
        userInfo.setAddress(user.getAddress1());
        userInfo.setPaymentMethod(user.getCardType());
        invoiceEntity.setUserInfo(userInfo);

        String json = new Gson().toJson(invoiceEntity);
        invoiceEntity.setInvoiceJson(json);
        return invoiceEntity;
    }

}

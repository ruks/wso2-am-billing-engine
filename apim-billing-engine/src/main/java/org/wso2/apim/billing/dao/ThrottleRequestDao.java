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
package org.wso2.apim.billing.dao;

import org.wso2.apim.billing.Util;
import org.wso2.apim.billing.bean.APIUsage;
import org.wso2.apim.billing.bean.AppApiSubscriptionBean;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.bean.SearchRequestBean;
import org.wso2.apim.billing.clients.APIRESTClient;
import org.wso2.apim.billing.clients.SPRestClient;
import org.wso2.apim.billing.domain.PlanEntity;
import org.wso2.apim.billing.domain.UserEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThrottleRequestDao {

    public static final String SP_REST_API = "/stores/query";
    private String apimStoreUrl;
    private String apimUserName;
    private String apimPassword;

    private String dasUrl;
    private String dasUserName;
    private String dasPassword;
    private PlanDao planDao;
    private String jksPath;
    private UsagePlanDao usagePlanDao;

    public ThrottleRequestDao() {

    }

    public String getJksPath() {
        return jksPath;
    }

    public void setJksPath(String jksPath) {
        this.jksPath = jksPath;
    }

    public PlanDao getPlanDao() {
        return planDao;
    }

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    public static String getSpRestApi() {
        return SP_REST_API;
    }

    public String getApimStoreUrl() {
        return apimStoreUrl;
    }

    public void setApimStoreUrl(String apimStoreUrl) {
        this.apimStoreUrl = apimStoreUrl;
    }

    public String getApimUserName() {
        return apimUserName;
    }

    public void setApimUserName(String apimUserName) {
        this.apimUserName = apimUserName;
    }

    public String getApimPassword() {
        return apimPassword;
    }

    public void setApimPassword(String apimPassword) {
        this.apimPassword = apimPassword;
    }

    public String getDasUrl() {
        return dasUrl;
    }

    public void setDasUrl(String dasUrl) {
        this.dasUrl = dasUrl;
    }

    public String getDasUserName() {
        return dasUserName;
    }

    public void setDasUserName(String dasUserName) {
        this.dasUserName = dasUserName;
    }

    public String getDasPassword() {
        return dasPassword;
    }

    public void setDasPassword(String dasPassword) {
        this.dasPassword = dasPassword;
    }

    private InvoiceEntity getInvoice(List<APIUsage> apiUsages, UserEntity user) {
        APIUsage apiUsage=apiUsages.get(0);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        String billDate = dateFormat.format(calendar.getTime());
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        String dueDate = dateFormat.format(calendar.getTime());

//        PlanEntity plan = planDao.loadPlanByPlanName(planName);

//        throttle = getThrottleCount(plan, apiUsage.getSuccessCount(), apiUsage.getExceedCount());

        List<BillingModel> packages = usagePlanDao.loadBillingPlansOfUser(user.getUserName());
        BillingModel plan = packages.get(0);

        double subscriptionFee = 0;
        double successFee = 0;
        double throttleFee = 0;
        double totalFee = 0;

        double feePerRequest = 0;
        double feePerThrottle = 0;

        int ran = (int) (Math.random() * 1000);

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setAddress1(user.getAddress1());
        invoiceEntity.setAddress2(user.getAddress2());
        invoiceEntity.setAddress3(user.getAddress3());
        invoiceEntity.setCreatedDate(billDate);
        invoiceEntity.setDueDate(dueDate);
        invoiceEntity.setInvoiceNo(ran);
        invoiceEntity.setPaymentMethod(user.getCardType());
        invoiceEntity.setSubscriptionFee(0);
        invoiceEntity.setSuccessCount(apiUsage.getSuccessCount());
        invoiceEntity.setSuccessFee(successFee);
        invoiceEntity.setThrottleCount(apiUsage.getExceedCount());
        invoiceEntity.setThrottleFee(throttleFee);
        invoiceEntity.setTotalFee(totalFee);
        invoiceEntity.setUserCompany(user.getCompany());
        invoiceEntity.setUserEmail(user.getEmail());
        invoiceEntity.setUserFirstName(user.getFirstName());
        invoiceEntity.setUserLastName(user.getLastName());
        invoiceEntity.setPlanName(plan.getPackageName());
        invoiceEntity.setFeePerSuccess(feePerRequest);
        invoiceEntity.setFeePerThrottle(feePerThrottle);
        invoiceEntity.setPlanType(plan.getPackageType());
        return invoiceEntity;
    }

    public InvoiceEntity GenerateInvoice(UserEntity user) {
        System.setProperty("javax.net.ssl.trustStore", jksPath);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        /*if (planName == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Plan name is not selected", "Sorry!"));
            return null;
        }*/

        try {
            SPRestClient s = new SPRestClient(this.dasUrl, this.dasUserName, this.dasPassword.toCharArray());

            //creating request bean
            String searchQuery = "from throttleInfoBillingAggregate on applicationOwner=='" + user.getUserName()
                    + "' within 0L, 2543913404000L per 'months' select apiName, apiVersion, "
                    + "subscriptionTier, applicationName, successCount, exceedCount";
            SearchRequestBean request = new SearchRequestBean();
            JSONObject reJsonObject = new JSONObject();
            reJsonObject.put("appName", "APIM_BILLING_INFO_SUMMARY");
            reJsonObject.put("query", searchQuery);
            request.setPayload(reJsonObject.toString());

            String resMsg = s.getResponse(request, this.dasUrl + SP_REST_API);
            System.out.println("response: " + resMsg);
            JSONObject obj = new JSONObject(resMsg);
            InvoiceEntity result;
            List<APIUsage> apiUsages = new ArrayList<APIUsage>();
            if(obj.length() != 0){
            	JSONArray records = obj.getJSONArray("records");
                for (int i = 0; i < records.length(); i++) {
                    APIUsage apiUsage = new APIUsage();
                    JSONArray row = records.getJSONArray(i);
                    System.out.println(row);
                    String apiName = row.getString(0);
                    String apiVersion = row.getString(1);
                    String SubscriptionTier = row.getString(2);
                    String applicationName = row.getString(3);
                    long successCount = row.getLong(4);
                    long exceedCount = row.getLong(5);
                    apiUsage.setApiName(apiName);
                    apiUsage.setVersion(apiVersion);
                    apiUsage.setSubscriptionTier(SubscriptionTier);
                    apiUsage.setApplicationName(applicationName);
                    apiUsage.setSuccessCount(successCount);
                    apiUsage.setExceedCount(exceedCount);
                    apiUsages.add(apiUsage);
                }
                result = getInvoice(apiUsages, user);
            } else {
            	result = new InvoiceEntity();
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getQuery(String planName) throws Exception {
        APIRESTClient r = new APIRESTClient();
        r.setUrls(this.apimStoreUrl);
        r.login(this.apimUserName, this.apimPassword);
        List<AppApiSubscriptionBean> beans = r.listSubscriptionBeans(planName);

        if (beans == null || beans.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        AppApiSubscriptionBean bean1 = beans.get(0);
        query.append("(").append("api:\"").append(bean1.getApiName()).append("\" AND ").append("applicationName:\"")
                .append(bean1.getAppName()).append("\" AND ").append("version:\"").append(bean1.getApiName())
                .append(":v").append(bean1.getVersion()).append("\" ) ");

        for (int i = 1; i < beans.size(); i++) {
            AppApiSubscriptionBean bean = beans.get(i);
            query.append("OR (").append("api:\"").append(bean.getApiName()).append("\" AND ")
                    .append("applicationName:\"").append(bean.getAppName()).append("\" AND ").append("version:\"")
                    .append(bean.getApiName()).append(":v").append(bean.getVersion()).append("\" ) ");
        }

        return query.toString();
    }

    private double getSuccessRequestFee(PlanEntity plan, long success) {
        if (plan.getPlanType().equals("STANDARD")) {
            return 0.0;
        } else if (plan.getPlanType().equals("USAGE")) {
            return success * plan.getFeePerRequest();
        } else {
            return 0.0;
        }
    }

    private double getThrottleRequestFee(PlanEntity plan, long throttle) {
        if (plan.getPlanType().equals("STANDARD")) {
            return throttle * plan.getFeePerRequest();
        } else if (plan.getPlanType().equals("USAGE")) {
            return 0.0;
        } else {
            return 0.0;
        }
    }

    private int getThrottleCount(PlanEntity plan, int success, int throttle) {
        if (plan.getPlanType().equals("STANDARD")) {
            int diff = success - Integer.parseInt(plan.getQuota());
            if (diff > 0) {
                return diff;
            } else {
                return success;
            }
        } else if (plan.getPlanType().equals("USAGE")) {
            return throttle;
        } else {
            return throttle;
        }
    }

    private double getPerSuccessFee(PlanEntity plan) {
        if (plan.getPlanType().equals("STANDARD")) {
            return 0.0;
        } else {
            return plan.getFeePerRequest();
        }
    }

    private double getPerThrottleFee(PlanEntity plan) {
        if (plan.getPlanType().equals("STANDARD")) {
            return plan.getFeePerRequest();
        } else {
            return 0.0;
        }
    }

    public UsagePlanDao getUsagePlanDao() {
        return usagePlanDao;
    }

    public void setUsagePlanDao(UsagePlanDao usagePlanDao) {
        this.usagePlanDao = usagePlanDao;
    }
}

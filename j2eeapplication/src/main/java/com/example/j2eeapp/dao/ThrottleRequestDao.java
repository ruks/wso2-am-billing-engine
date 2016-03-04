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
package com.example.j2eeapp.dao;

import bean.AggregateField;
import bean.AppApiSubscriptionBean;
import bean.Invoice;
import bean.SearchRequestBean;
import com.example.j2eeapp.clients.APIRESTClient;
import com.example.j2eeapp.clients.DASRestClient;
import com.example.j2eeapp.domain.PlanEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThrottleRequestDao {

    public static final String DAS_AGGREGATES_SEARCH_REST_API_URL = "/analytics/aggregates";
    private String apimStoreUrl;
    private String apimUserName;
    private String apimPassword;

    private String dasUrl;
    private String dasUserName;
    private String dasPassword;

    public static String getDasAggregatesSearchRestApiUrl() {
        return DAS_AGGREGATES_SEARCH_REST_API_URL;
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

    static {
        System.setProperty("javax.net.ssl.trustStore", "/home/rukshan/wso2-jks/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
    }

    public Invoice getInvoice(int success, int throttle, String planName) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

//        PlanDao planDao= new PlanJpaDao();
//        PlanEntity plan=planDao.loadPlanByPlanName(planName);
        PlanEntity plan=new PlanEntity();
        plan.setFee("100");
        plan.setAdfee("10");

        double subscriptionFee= Double.parseDouble(plan.getFee());
        double successFee = 0;
        double throttleFee = throttle*Double.parseDouble(plan.getAdfee());
        double totalFee = subscriptionFee+successFee+throttleFee;

        Invoice invoice=new Invoice();
        invoice.setAddress1("address1");
        invoice.setAddress2("address2");
        invoice.setAddress3("address2");
        invoice.setCreatedDate(dateFormat.format(date));
        invoice.setDuedDate(dateFormat.format(date));
        invoice.setInvoiceNo(1);
        invoice.setPaymentMethod("Cash");
        invoice.setSubscriptionFee(plan.getFee());
        invoice.setSuccessCount(success);
        invoice.setSuccessFee(successFee+"");
        invoice.setThrottleCount(throttle);
        invoice.setThrottleFee(throttleFee+"");
        invoice.setTotalFee(totalFee+"");
        invoice.setUserCompany("example company");
        invoice.setUserEmail("user@example.com");
        invoice.setUserFirstName("first name");
        invoice.setUserLastName("last name");

        return invoice;
    }

    public Invoice getCount(String planName) {

        if(planName==null){
            planName="silver";
        }

        try {
            DASRestClient s = new DASRestClient(this.dasUrl, this.apimUserName, this.dasPassword.toCharArray());

            String query = getQuery(planName);//"tenantDomain" + ":\"" + "admin@carbon.super" + "\"";
            if(query==null){
                System.out.println("no subscription for plan: "+planName);
                return null;
            }

            //creating request bean
            SearchRequestBean request = new SearchRequestBean(query, 1, "tenantDomain_userId_facet",
                    "THROTTLED_SUMMARY");
            ArrayList<AggregateField> fields = new ArrayList<AggregateField>();
            AggregateField field = new AggregateField("success_request_count", "sum", "sCount");
            AggregateField field2 = new AggregateField("throttleout_count", "sum", "tCount");
            fields.add(field);
            fields.add(field2);
            request.setAggregateFields(fields);

            CloseableHttpResponse res = s.doPost(request, this.dasUrl + DAS_AGGREGATES_SEARCH_REST_API_URL);
            String resMsg = getResponseBody(res);
            JSONArray obj = new JSONArray(resMsg);
            JSONObject val = obj.getJSONObject(0).getJSONObject("values");
            int scount = val.getInt("sCount");
            int tcount = val.getInt("tCount");

            System.out.println(scount);
            System.out.println(tcount);

//            return "Sucess: "+scount+" Throttle: "+tcount;
            return getInvoice(scount,tcount,planName);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getResponseBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        return sb.toString();
    }

    private String getQuery(String planName) throws Exception {
        APIRESTClient r=new APIRESTClient(this.apimStoreUrl);
        r.login(this.apimUserName,this.apimPassword);
        List<AppApiSubscriptionBean> beans=r.listSubscriptionBeans(planName);

        if (beans==null || beans.isEmpty()){
            return null;
        }
        StringBuilder query=new StringBuilder();
        AppApiSubscriptionBean bean1 = beans.get(0);
        query.append("(")
                .append("api:\"").append(bean1.getApiName()).append("\" AND ")
                .append("applicationName:\"").append(bean1.getAppName()).append("\" AND ")
                .append("version:\"").append(bean1.getApiName()).append(":v").append(bean1.getVersion()).append("\" ) ");

        for (int i = 1; i < beans.size(); i++) {
            AppApiSubscriptionBean bean = beans.get(i);
            query.append("OR (")
                    .append("api:\"").append(bean.getApiName()).append("\" AND ")
                    .append("applicationName:\"").append(bean.getAppName()).append("\" AND ")
                    .append("version:\"").append(bean1.getApiName()).append(":v").append(bean.getVersion()).append("\" ) ");
        }

        return query.toString();
    }
}

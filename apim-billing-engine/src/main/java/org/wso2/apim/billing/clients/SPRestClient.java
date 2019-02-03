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
package org.wso2.apim.billing.clients;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.apim.billing.Util;
import org.wso2.apim.billing.bean.APIUsage;
import org.wso2.apim.billing.bean.SearchRequestBean;
import org.wso2.apim.billing.domain.BillingModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SPRestClient {
    private CloseableHttpClient httpClient;
    private String spHostUrl;
    private String user;
    private char[] pass;
    private String spTrustStore;
    private char[] spTrustStorePassword;
    private static final Log log = LogFactory.getLog(SPRestClient.class);
    public static final String HTTP_AUTH_HEADER_NAME = "Authorization";
    public static final String HTTP_AUTH_HEADER_TYPE = "Basic";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SP_REST_API = "/stores/query";

    public SPRestClient() {

    }

    public SPRestClient(String spHostUrl, String user, char[] pass, String spTrustStore, char[] spTrustStorePassword) {
        this.spHostUrl = spHostUrl;
        this.user = user;
        this.pass = pass;
        this.spTrustStore = spTrustStore;
        this.spTrustStorePassword = spTrustStorePassword;
        this.httpClient = Util.initHttpClient(this.spTrustStore, this.spTrustStorePassword);
    }

    /**
     * Do a post request to the DAS REST
     *
     * @param request lucene json request
     * @param url     DAS rest api location
     * @return return the HttpResponse after the request sent
     * @throws IOException throw if the connection exception occur
     */
    public String getResponse(SearchRequestBean request, String url) throws IOException {
        String json = request.getPayload();
        System.out.println(json);
        System.out.println(url);
        if (log.isDebugEnabled()) {
            log.debug("Sending Lucene Query : " + json);
        }
        HttpPost postRequest = new HttpPost(url);
        HttpContext context = HttpClientContext.create();

        //get the encoded basic authentication
        String cred = encodeCredentials(this.user, this.pass);
        postRequest.addHeader(HTTP_AUTH_HEADER_NAME, HTTP_AUTH_HEADER_TYPE + ' ' + cred);
        StringEntity input = new StringEntity(json);
        input.setContentType(APPLICATION_JSON);
        postRequest.setEntity(input);

        //send the request
        CloseableHttpResponse res = httpClient.execute(postRequest, context);
        System.out.println(res.getStatusLine().getStatusCode());
        if (200 == res.getStatusLine().getStatusCode()) {
            return getResponseBody(res);
        } else {
            throw new IOException("Error occurred while getting proper response.");
        }
    }

    /**
     * get the base 64 encoded username and password
     *
     * @param user username
     * @param pass password
     * @return encoded basic auth, as string
     */
    public String encodeCredentials(String user, char[] pass) {
        StringBuilder builder = new StringBuilder(user).append(':').append(pass);
        String cred = builder.toString();
        byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
        return new String(encodedBytes);
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

    public List<APIUsage> getUsagePerPackage(String user, BillingModel billingModel) throws IOException {
        String searchQuery = "from throttleInfoBillingAggregate on applicationOwner=='" + user + "' " + "and apiName=='"
                + billingModel.getPlan().getApiName() + "' and apiVersion=='" + billingModel.getPlan().getApiVersion()
                + "' " + "and subscriptionTier=='" + billingModel.getPlan().getThrottlePolicy()
                + "' within 0L, 2543913404000L "
                + "per 'months' select apiName, apiVersion,subscriptionTier, applicationName, successCount, exceedCount";
        SearchRequestBean request = new SearchRequestBean();
        JSONObject reJsonObject = new JSONObject();
        reJsonObject.put("appName", "APIM_BILLING_INFO_SUMMARY");
        reJsonObject.put("query", searchQuery);
        request.setPayload(reJsonObject.toString());

        String resMsg = this.getResponse(request, this.spHostUrl + SP_REST_API);
        System.out.println("response: " + resMsg);
        JSONObject obj = new JSONObject(resMsg);
        List<APIUsage> apiUsages = new ArrayList<>();
        if (obj.length() != 0) {
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
        }
        return apiUsages;
    }

    public String getSpHostUrl() {
        return spHostUrl;
    }

    public void setSpHostUrl(String spHostUrl) {
        this.spHostUrl = spHostUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public char[] getPass() {
        return pass;
    }

    public void setPass(char[] pass) {
        this.pass = pass;
    }

    public String getSpTrustStore() {
        return spTrustStore;
    }

    public void setSpTrustStore(String spTrustStore) {
        this.spTrustStore = spTrustStore;
    }

    public char[] getSpTrustStorePassword() {
        return spTrustStorePassword;
    }

    public void setSpTrustStorePassword(char[] spTrustStorePassword) {
        this.spTrustStorePassword = spTrustStorePassword;
    }


}

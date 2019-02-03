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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.apim.billing.Util;
import org.wso2.apim.billing.model.BasicAPIInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIRESTClient {
    private static final Log log = LogFactory.getLog(APIRESTClient.class);
    private final String USER_AGENT = "Apache-HttpClient/4.2.5 (java 1.5)";
    private final CloseableHttpClient httpClient;
    private String publisherUrl;
    private String apimTrustStore;
    private String apimTrustStorePassword;

    public APIRESTClient(String publisherUrl, String apimTrustStore, String apimTrustStorePassword) {
        this.publisherUrl = publisherUrl;
        this.apimTrustStore = apimTrustStore;
        this.apimTrustStorePassword = apimTrustStorePassword;
        this.httpClient = Util.initHttpClient(this.apimTrustStore, this.apimTrustStorePassword.toCharArray());
    }

    public Map<String, List<BasicAPIInfo>> getApis(String token) {
        Map<String, List<BasicAPIInfo>> apiInfoMap = new HashMap<String, List<BasicAPIInfo>>();
        HttpGet httpPost = new HttpGet(publisherUrl);
        try {
            httpPost.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Get APIs: " + response.getStatusLine().getStatusCode());
            JSONObject jsonObject = new JSONObject(content);
            JSONArray apis = jsonObject.getJSONArray("list");

            List<BasicAPIInfo> apiInfoList;
            JSONObject api;
            String apiName;
            for (int i = 0; i < apis.length(); i++) {
                api = apis.getJSONObject(i);
                apiName = api.getString("name");
                if (apiInfoMap.containsKey(apiName)) {
                    apiInfoList = apiInfoMap.get(apiName);
                } else {
                    apiInfoList = new ArrayList<BasicAPIInfo>();
                }
                BasicAPIInfo info = new BasicAPIInfo();
                info.setId(api.getString("id"));
                info.setName(apiName);
                info.setVersion(api.getString("version"));
                apiInfoList.add(info);
                apiInfoMap.put(apiName, apiInfoList);
            }
            return apiInfoMap;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiInfoMap;
    }

    public List<String> getApiThrottlePolicies(String token, String apiId) {
        List<String> policies = new ArrayList<String>();
        HttpGet httpPost = new HttpGet(publisherUrl + "/" + apiId);
        try {
            httpPost.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Get API Details: " + response.getStatusLine().getStatusCode());
            JSONObject jsonObject = new JSONObject(content);
            JSONArray policyArr = jsonObject.getJSONArray("tiers");

            String policy;
            for (int i = 0; i < policyArr.length(); i++) {
                policy = policyArr.getString(i);
                policies.add(policy);
            }
            return policies;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return policies;
    }

    public String getPublisherUrl() {
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public String getApimTrustStore() {
        return apimTrustStore;
    }

    public void setApimTrustStore(String apimTrustStore) {
        this.apimTrustStore = apimTrustStore;
    }

    public String getApimTrustStorePassword() {
        return apimTrustStorePassword;
    }

    public void setApimTrustStorePassword(String apimTrustStorePassword) {
        this.apimTrustStorePassword = apimTrustStorePassword;
    }
}

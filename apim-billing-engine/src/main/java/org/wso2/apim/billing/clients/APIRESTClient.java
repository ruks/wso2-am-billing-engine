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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.apim.billing.bean.AppApiSubscriptionBean;
import org.wso2.apim.billing.model.BasicAPIInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIRESTClient {
    private final String USER_AGENT = "Apache-HttpClient/4.2.5 (java 1.5)";
    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    String loginUrl;
    String getSubsUrl;
    String appListUrl;
    private final CloseableHttpClient httpClient;
    HttpResponse response;
    String validTiers = "Unlimited,Gold,Silver,Bronze";
    private static final Log log = LogFactory.getLog(APIRESTClient.class);
    private String publisherUrl;

    public APIRESTClient() {
        // TODO Auto-generated constructor stub
        httpClient = HttpClients.custom().setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
    }

    public void setUrls(String host) {
        loginUrl = host + "/store/site/blocks/user/login/ajax/login.jag";
        getSubsUrl = host + "/store/site/blocks/subscription/subscription-list/ajax/subscription-list.jag";
        appListUrl = host + "/store/site/blocks/application/application-list/ajax/application-list.jag";
    }

    public List<AppApiSubscriptionBean> listSubscriptionBeans(String planName) throws Exception {
        List<AppApiSubscriptionBean> list = new ArrayList<AppApiSubscriptionBean>();
        ArrayList<String> apps = getApplications();
        for (String app : apps) {
            String sub = getSubscriptionByApp(app);
            JSONArray apis = new JSONObject(sub).getJSONArray("apis");
            for (int i = 0; i < apis.length(); i++) {
                JSONObject api = apis.getJSONObject(i);

                String tier = api.getString("subscribedTier");
                if (planName.equalsIgnoreCase(tier)) {
                    String apiName = api.getString("apiName");
                    String version = api.getString("apiVersion");
                    AppApiSubscriptionBean bean = new AppApiSubscriptionBean();
                    bean.setTier(tier);
                    bean.setApiName(apiName);
                    bean.setVersion(version);
                    bean.setAppName(app);
                    list.add(bean);
                }
            }
        }
        return list;
    }

    public String getSubscriptionByApp(String app) throws Exception {
        response = sendGetRequest(getSubsUrl + "?action=getSubscriptionByApplication&app=" + app);
        String res = getResponseBody(response);
        EntityUtils.consume(response.getEntity());
        return res;
    }

    public ArrayList<String> getApplications() throws Exception {
        response = sendGetRequest(appListUrl + "?action=getApplications");
        String res = getResponseBody(response);
        EntityUtils.consume(response.getEntity());
        JSONObject resObj = new JSONObject(res);
        JSONArray appArr = resObj.getJSONArray("applications");
        ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < appArr.length(); i++) {
            list.add(appArr.getJSONObject(i).getString("name"));
        }
        return list;
    }

    private HttpResponse sendGetRequest(String url) throws Exception {
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept", "application/json");
        //		request.addHeader("Authorization", "Bearer " + bearer);
        return httpClient.execute(request);
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

    public void login(String user, String pass) throws Exception {
        // TODO Auto-generated method stub
        urlParameters.add(new BasicNameValuePair("action", "login"));
        urlParameters.add(new BasicNameValuePair("username", user));
        urlParameters.add(new BasicNameValuePair("password", pass));

        response = sendPOSTMessage(loginUrl, urlParameters);
        System.out.println(getResponseBody(response));
        EntityUtils.consume(response.getEntity());
    }

    private HttpResponse sendPOSTMessage(String url, List<NameValuePair> urlParameters) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.addHeader("Referer", url);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return httpClient.execute(post);
    }

    public static void main(String[] args) {
        TokenManager tokenManager = new TokenManager();
        String token = tokenManager.getToken();
        System.out.println(token);

        APIRESTClient apirestClient = new APIRESTClient();
        apirestClient.setPublisherUrl("https://localhost:9443/api/am/publisher/v0.14/apis");
        Map<String, List<BasicAPIInfo>> apis = apirestClient.getApis(token);
        List<String> policies = apirestClient.getApiThrottlePolicies(token, "3b7a6666-e374-4f97-a606-63deae60d1fe");

        System.out.println(apis.size());
        System.out.println(policies.size());
    }

    public Map<String, List<BasicAPIInfo>> getApis(String token) {
        Map<String, List<BasicAPIInfo>> apiInfoMap = new HashMap<String, List<BasicAPIInfo>>();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(publisherUrl);
        try {
            httpPost.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = client.execute(httpPost);
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
            client.close();
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
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(publisherUrl + "/" + apiId);
        try {
            httpPost.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = client.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Get API Details: " + response.getStatusLine().getStatusCode());
            JSONObject jsonObject = new JSONObject(content);
            JSONArray policyArr = jsonObject.getJSONArray("tiers");

            String policy;
            for (int i = 0; i < policyArr.length(); i++) {
                policy = policyArr.getString(i);
                policies.add(policy);
            }
            client.close();
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
}

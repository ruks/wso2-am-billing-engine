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
package com.example.j2eeapp.clients;

import bean.AppApiSubscriptionBean;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class APIRESTClient {
    private final String USER_AGENT = "Apache-HttpClient/4.2.5 (java 1.5)";
    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    String loginUrl;
    String getSubsUrl;
    String appListUrl;
    private final CloseableHttpClient httpClient;
    HttpResponse response;
    String validTiers= "Unlimited,Gold,Silver,Bronze";

    public APIRESTClient(String host) {
        // TODO Auto-generated constructor stub
        httpClient = HttpClients.custom()
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();

        loginUrl = host + "/store/site/blocks/user/login/ajax/login.jag";
        getSubsUrl = host + "/store/site/blocks/subscription/subscription-list/ajax/subscription-list.jag";
        appListUrl = host + "/store/site/blocks/application/application-list/ajax/application-list.jag";
        System.out.println("host "+host);
    }

    public List<AppApiSubscriptionBean> listSubscriptionBeans(String planName) throws Exception {
        List<AppApiSubscriptionBean> list=new ArrayList<AppApiSubscriptionBean>();
        ArrayList<String> apps=getApplications();
        for (String app : apps) {
            String sub=getSubscriptionByApp(app);
            JSONArray apis=new JSONObject(sub).getJSONArray("apis");
            for (int i = 0; i < apis.length(); i++) {
                JSONObject api=apis.getJSONObject(i);

                String tier=api.getString("subscribedTier");
                if(planName.equalsIgnoreCase(tier)){
                    String apiName=api.getString("apiName");
                    String version=api.getString("apiVersion");
                    AppApiSubscriptionBean bean=new AppApiSubscriptionBean();
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
        response = sendGetRequest(getSubsUrl+"?action=getSubscriptionByApplication&app="+app);
        String res=getResponseBody(response);
        EntityUtils.consume(response.getEntity());
        return res;
    }

    public ArrayList<String> getApplications() throws Exception {
        response = sendGetRequest(appListUrl+"?action=getApplications");
        String res=getResponseBody(response);
        EntityUtils.consume(response.getEntity());
        JSONObject resObj=new JSONObject(res);
        JSONArray appArr=resObj.getJSONArray("applications");
        ArrayList<String> list=new ArrayList<String>();

        for (int i = 0; i < appArr.length(); i++) {
            list.add(appArr.getJSONObject(i).getString("name"));
        }
        return list;
    }

    private HttpResponse sendGetRequest(String url)
            throws Exception {
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Accept", "application/json");
        //		request.addHeader("Authorization", "Bearer " + bearer);
        return httpClient.execute(request);
    }
    private String getResponseBody(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent(), "UTF-8"));
        String line;
        StringBuffer sb = new StringBuffer();

        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        return sb.toString();
    }
    public void login(String user,String pass) throws Exception {
        // TODO Auto-generated method stub
        urlParameters.add(new BasicNameValuePair("action", "login"));
        urlParameters.add(new BasicNameValuePair("username", user));
        urlParameters.add(new BasicNameValuePair("password", pass));

        response = sendPOSTMessage(loginUrl, urlParameters);
        System.out.println(getResponseBody(response));
        EntityUtils.consume(response.getEntity());
    }
    private HttpResponse sendPOSTMessage(String url,
            List<NameValuePair> urlParameters) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        post.addHeader("Referer", url);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        return httpClient.execute(post);
    }

}

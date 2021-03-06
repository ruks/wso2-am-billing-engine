package org.wso2.apim.billing.clients;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.wso2.apim.billing.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenManager {
    private String applicationName = "billing_app";
    private String consumerKey;
    private String consumerSecret;
    private String userName;
    private String password;
    private String tokenUrl;
    private String dcrUrl;
    private String introspectUrl;
    private String refreshToken;
    private String accessToken;
    private String apimTrustStore;
    private String apimTrustStorePassword;
    private CloseableHttpClient httpClient;
    private static final Log log = LogFactory.getLog(TokenManager.class);

    public TokenManager(String apimTrustStore, String apimTrustStorePassword) {
        this.apimTrustStore = apimTrustStore;
        this.apimTrustStorePassword = apimTrustStorePassword;
        this.httpClient = Util.initHttpClient(this.apimTrustStore, this.apimTrustStorePassword.toCharArray());
    }

    public String getToken() {
        createApp();
        getToken(getConsumerKey(), getConsumerSecret());
        boolean a = validateToken(getAccessToken());
        System.out.println(a);
        return getAccessToken();
    }

    public void createApp() {
        JSONObject request = new JSONObject();
        request.put("callbackUrl", "www.google.lk");
        request.put("clientName", "rest_api_publisher");
        request.put("owner", userName);
        request.put("grantType", "password refresh_token");
        request.put("saasApp", true);

        HttpPost httpPost = new HttpPost(dcrUrl);

        httpPost.setEntity(new StringEntity(request.toString(), ContentType.DEFAULT_TEXT));
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
        try {
            httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            log.debug("Create oauth app: " + response.getStatusLine().getStatusCode());
            String content = IOUtils.toString(response.getEntity().getContent());
            JSONObject jsonObject = new JSONObject(content);
            consumerKey = jsonObject.getString("clientId");
            consumerSecret = jsonObject.getString("clientSecret");
            return;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getToken(String consumerKey, String consumerSecret) {

        HttpPost httpPost = new HttpPost(tokenUrl);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", userName));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("scope", "apim:api_view"));

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(consumerKey, consumerSecret);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Generate token: " + response.getStatusLine().getStatusCode());
            JSONObject jsonObject = new JSONObject(content);
            refreshToken = jsonObject.getString("refresh_token");
            accessToken = jsonObject.getString("access_token");
            return;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateToken(String accessToken) {
        HttpPost httpPost = new HttpPost(introspectUrl);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", accessToken));

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, password);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.addHeader(new BasicScheme().authenticate(creds, httpPost, null));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = this.httpClient.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Validate token: " + response.getStatusLine().getStatusCode());
            JSONObject jsonObject = new JSONObject(content);
            boolean isActive = jsonObject.getBoolean("active");
            return isActive;
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getDcrUrl() {
        return dcrUrl;
    }

    public void setDcrUrl(String dcrUrl) {
        this.dcrUrl = dcrUrl;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIntrospectUrl() {
        return introspectUrl;
    }

    public void setIntrospectUrl(String introspectUrl) {
        this.introspectUrl = introspectUrl;
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

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

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.wso2.apim.billing.bean.SearchRequestBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SPRestClient {
    private CloseableHttpClient httpClient;
    private String dasUrl;
    private String user;
    private char[] pass;
    private final Gson gson = new Gson();
    private static final Log log = LogFactory.getLog(SPRestClient.class);
    public static final String HTTP_AUTH_HEADER_NAME = "Authorization";
    public static final String HTTP_AUTH_HEADER_TYPE = "Basic";
    public static final String APPLICATION_JSON = "application/json";

    /**
     * get instance providing DAS configuration
     *
     * @param url  DAS rest api location
     * @param user DAS rest api username
     * @param pass DAs rest api password
     */
    public SPRestClient(String url, String user, char[] pass) {
        httpClient = HttpClients.custom().setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
        this.dasUrl = url;
        this.user = user;
        this.pass = pass;
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
        CloseableHttpResponse res  = httpClient.execute(postRequest, context);
        System.out.println(res.getStatusLine().getStatusCode());
        if(200 == res.getStatusLine().getStatusCode()) {
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
}

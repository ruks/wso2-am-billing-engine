package org.wso2.sample.apimgt.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.sample.apimgt.BilllingEngineConnector;
import org.wso2.sample.apimgt.WorkflowConfiguration;

import java.io.IOException;

public class BilllingEngineConnectorImpl implements BilllingEngineConnector {
    private static final Log log = LogFactory.getLog(BilllingEngineConnectorImpl.class);

    public boolean checkSubscription(String apiId, String tier, String subscriber) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(WorkflowConfiguration.getInstance().getBillingEngineUrl() + "/apis");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", subscriber);
            jsonObject.put("api_id", apiId);
            jsonObject.put("tier", tier);
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            CloseableHttpResponse response = client.execute(httpPost);
            String content = IOUtils.toString(response.getEntity().getContent());
            log.debug("Check Billing Subscription: " + response.getStatusLine().getStatusCode());
            client.close();
            JSONObject jsonResponseObj = new JSONObject(content);
            return jsonResponseObj.getBoolean("active");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}

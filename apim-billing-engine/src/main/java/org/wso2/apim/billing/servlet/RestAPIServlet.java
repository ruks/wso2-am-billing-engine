package org.wso2.apim.billing.servlet;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wso2.apim.billing.services.SubscriptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAPIServlet extends HttpServlet {
    private SubscriptionService subscriptionService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // reading the user input
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(200);
        PrintWriter out = response.getWriter();

        //create Json Object
        JSONObject json = new JSONObject();

        // put some value pairs into the JSON object .
        json.put("active", true);

        // finally output the json string
        out.print(json.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        subscriptionService = (SubscriptionService) webApplicationContext.getBean("subscriptionService");
        try {
            String json = IOUtils.toString(request.getInputStream());
            JSONObject requestJson = new JSONObject(json);
            String userID = requestJson.getString("user_id");
            String apiID = requestJson.getString("api_id");
            String tier = requestJson.getString("tier");

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            PrintWriter out = response.getWriter();
            JSONObject responseJson = new JSONObject();
            responseJson.put("active", subscriptionService.checkSubscription(userID, apiID, tier));
            out.print(responseJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}

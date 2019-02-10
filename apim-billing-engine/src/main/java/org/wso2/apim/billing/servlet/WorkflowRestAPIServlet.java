package org.wso2.apim.billing.servlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wso2.apim.billing.dao.WorkflowDao;
import org.wso2.apim.billing.domain.SubsWorkflowDTO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WorkflowRestAPIServlet extends HttpServlet {
    private WorkflowDao workflowDao;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        workflowDao = (WorkflowDao) webApplicationContext.getBean("workflowDao");

        String workflowRefId = request.getParameter("workflowRefId");
        String callbackUrl = request.getParameter("callbackUrl");
        String apiName = request.getParameter("apiName");
        String apiVersion = request.getParameter("apiVersion");
        String apiId = request.getParameter("apiId");
        String subscriptionTier = request.getParameter("subscriptionTier");
        String application = request.getParameter("application");
        String subscriber = request.getParameter("subscriber");


        SubsWorkflowDTO subsWorkflowDTO = new SubsWorkflowDTO();
        subsWorkflowDTO.setWorkflowRefId(workflowRefId);
        subsWorkflowDTO.setCallbackUrl(callbackUrl);
        subsWorkflowDTO.setApiName(apiName);
        subsWorkflowDTO.setApiVersion(apiVersion);
        subsWorkflowDTO.setApiId(apiId);
        subsWorkflowDTO.setSubscriptionTier(subscriptionTier);
        subsWorkflowDTO.setApplication(application);
        subsWorkflowDTO.setSubscriber(subscriber);

        workflowDao.save(subsWorkflowDTO);
        try {
            response.sendRedirect("/apim-billing-engine-1.4.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

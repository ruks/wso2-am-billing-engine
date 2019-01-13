package org.wso2.sample.apimgt.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.apimgt.impl.APIManagerConfiguration;
import org.wso2.carbon.apimgt.impl.APIManagerConfigurationService;
import org.wso2.sample.apimgt.WorkflowConfiguration;

/**
 * @scr.component name="org.wso2.sample.apimgt.billing.services" immediate="true"
 * @scr.reference name="api.manager.config.service"
 * interface="org.wso2.carbon.apimgt.impl.APIManagerConfigurationService" cardinality="1..1"
 * policy="dynamic" bind="setAPIManagerConfigurationService" unbind="unsetAPIManagerConfigurationService"
 */
public class BillingEngineWorkflowComponent {
    private static final Log log = LogFactory.getLog(BillingEngineWorkflowComponent.class);

    private static APIManagerConfiguration configuration = null;

    protected void activate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("BillingEngineWorkflowComponent activated");
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        if (log.isDebugEnabled()) {
            log.debug("BillingEngineWorkflowComponent deactivated");
        }
    }

    protected void setAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
        if (log.isDebugEnabled()) {
            log.debug("WebApp manager configuration service bound to the WebApp host objects");
        }
        configuration = amcService.getAPIManagerConfiguration();
        String billingEngineUrl = configuration.getFirstProperty("BillingEngine.BillingEngineUrl");
        String apimStoreUrl = configuration.getFirstProperty("BillingEngine.ApimStoreUrl");
        WorkflowConfiguration.getInstance().setBillingEngineUrl(billingEngineUrl);
        WorkflowConfiguration.getInstance().setApimStoreUrl(apimStoreUrl);

    }

    protected void unsetAPIManagerConfigurationService(APIManagerConfigurationService amcService) {
        if (log.isDebugEnabled()) {
            log.debug("WebApp manager configuration service unbound from the WebApp host objects");
        }
        configuration = null;
    }
}

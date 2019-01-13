package org.wso2.apim.billing.services.impl;

import org.wso2.apim.billing.Constants;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingModel;
import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.services.CostCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostCalculatorImpl implements CostCalculator {
    public void calculateCost(List<BillingModel> packages) {

    }

    private Map<String, String> processMeteredPackages(BillingModel billingModel) {
        Map<String, String> displayValues = new HashMap<>();
        displayValues.put(Constants.packageDisplayName, billingModel.getPackageName());
        displayValues.put(Constants.packageType, billingModel.getPackageType());

        List<BillingAttribute> attributes = billingModel.getAttributes();
        for (BillingAttribute billingAttribute : attributes) {
            switch (billingAttribute.getKey()) {
            case Constants.feePerRequestKey:
                displayValues.put(Constants.feePerRequest, billingAttribute.getValue());
                break;
            case Constants.subcriptionFeeKey:
                displayValues.put(Constants.subscriptionFee, billingAttribute.getValue());
                break;
            }
        }
        displayValues.put(Constants.TotalAPIRequest, "10");
        displayValues.put(Constants.totalRequestCost, "10");
        displayValues.put(Constants.totalCost, "20");
        return displayValues;
    }

    private Map<String, String> processQoutaPackages(BillingModel billingModel) {
        Map<String, String> displayValues = new HashMap<>();
        displayValues.put(Constants.packageDisplayName, billingModel.getPackageName());
        displayValues.put(Constants.packageType, billingModel.getPackageType());

        List<BillingAttribute> attributes = billingModel.getAttributes();
        for (BillingAttribute billingAttribute : attributes) {
            switch (billingAttribute.getKey()) {
            case Constants.quota:
                displayValues.put(Constants.quota_display_name, billingAttribute.getValue());
                break;
            case Constants.quotaPrice:
                displayValues.put(Constants.quotaPrice_display_name, billingAttribute.getValue());
                break;
            case Constants.excessivePrice:
                displayValues.put(Constants.excessivePrice_display_name, billingAttribute.getValue());
                break;
            case Constants.subcriptionFeeKey:
                displayValues.put(Constants.subscriptionFee, billingAttribute.getValue());
                break;

            }
        }
        displayValues.put(Constants.TotalAPIRequest, "10");
        displayValues.put(Constants.totalExtraRequest, "10");
        displayValues.put(Constants.costForQuota, "10");
        displayValues.put(Constants.costForExtraRequest, "10");
        displayValues.put(Constants.totalCost, "30");
        return displayValues;
    }
}

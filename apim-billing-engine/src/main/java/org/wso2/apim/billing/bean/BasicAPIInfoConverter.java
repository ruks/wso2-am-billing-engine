package org.wso2.apim.billing.bean;

import org.primefaces.component.inputtext.InputText;
import org.wso2.apim.billing.domain.BillingAttribute;
import org.wso2.apim.billing.domain.BillingPlan;
import org.wso2.apim.billing.model.BasicAPIInfo;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class BasicAPIInfoConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String id) {
        BillingPlan billingPlan = getBean("usagePlan", BillingPlan.class);
        Object ver = component.getAttributes().get(id);
        if(ver !=null) {
            BasicAPIInfo basicAPIInfo = new BasicAPIInfo();
            basicAPIInfo.setId(id);
            basicAPIInfo.setVersion((String) ver);
//            billingPlan.setBasicAPIInfo(basicAPIInfo);
            return basicAPIInfo;
        }
        return null;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        BillingPlan billingPlan = getBean("usagePlan", BillingPlan.class);

        if (value != null && !(value instanceof String)) {
            BasicAPIInfo basicAPIInfo = (BasicAPIInfo) value;
            component.getAttributes().put(basicAPIInfo.getId(), basicAPIInfo.getVersion());
            return basicAPIInfo.getId();
        }
        return "";
    }

    public static <T> T getBean(final String beanName, final Class<T> clazz) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (T) FacesContext.getCurrentInstance().getApplication().getELResolver()
                .getValue(elContext, null, beanName);
    }
}

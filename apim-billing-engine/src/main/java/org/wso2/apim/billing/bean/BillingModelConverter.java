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
package org.wso2.apim.billing.bean;

import org.primefaces.component.inputtext.InputText;
import org.wso2.apim.billing.domain.BillingAttribute;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class BillingModelConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String name = ((InputText) component).getLabel();
        String displayName = ((InputText) component).getTitle();
        BillingAttribute billingAttribute = new BillingAttribute(1, name, displayName, value);
        return billingAttribute;
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value != null) {
            BillingAttribute billingAttribute = (BillingAttribute) value;
            return billingAttribute.getValue();
        }
        return "";
    }
}

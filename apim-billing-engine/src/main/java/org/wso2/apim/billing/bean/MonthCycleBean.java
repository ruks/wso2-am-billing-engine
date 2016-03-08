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

import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.domain.PlanEntity;
import org.wso2.apim.billing.services.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class MonthCycleBean implements Serializable {

    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public Map<String, Object> loadListOfMonths() {
        LinkedHashMap<String, Object> monthList = new LinkedHashMap<String, Object>();

        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length - 1; i++) {
            String month = months[i];
            monthList.put(month, month);
        }
        return monthList;
    }

}

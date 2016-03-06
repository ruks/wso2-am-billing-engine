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
package bean;

import com.example.j2eeapp.dao.PlanDao;
import com.example.j2eeapp.domain.PlanEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class PlanConverter implements Converter {

    private PlanDao planDAO;

    public PlanDao getPlanDAO() {
        return planDAO;
    }

    public void setPlanDAO(PlanDao planDAO) {
        this.planDAO = planDAO;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("36 "+value);
        return planDAO.loadPlanByPlanName(value);
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        System.out.println("41" + value);
        String name= ((PlanEntity) value).getPlanName();
        if(name == null){
            return null;
        }
        return name.toString();
    }
}

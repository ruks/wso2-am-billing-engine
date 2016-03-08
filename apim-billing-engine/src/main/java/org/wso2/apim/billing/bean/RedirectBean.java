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

import org.springframework.webflow.execution.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class RedirectBean implements Serializable {

    String reDirectUrl;

    public String getReDirectUrl() {
        return reDirectUrl;
    }

    public void setReDirectUrl(String reDirectUrl) {
        this.reDirectUrl = reDirectUrl;
    }

    public String getUrl() {
//        String s=context.getRequestParameters().asAttributeMap().toString();
//        System.out.println(s);
//        System.out.println("----------------------------------------------------------------------------------------------------------here");

//        FacesContext ctx = FacesContext.getCurrentInstance();
//        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
//        String fullURI = servletRequest.getRequestURI();
//        System.out.println(fullURI);
//        System.out.println(ctx.getExternalContext().getRequestParameterMap().get("CallbackUrl"));
//        "finish or externalRedirect:yes";
        return "https://localhost:9443/store/";
    }

    public String eval() {
        System.out.println("----------------------------------------------------------------------------------------------------------here");
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest servletRequest = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String fullURI = servletRequest.getRequestURI();

        System.out.println(ctx.getExternalContext().getRequestParameterMap().get("CallbackUrl"));
        return "Https://www.google.lk";
    }
}

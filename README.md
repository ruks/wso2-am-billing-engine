        ================================================================================
                                        Simple Billing Engine for WSO2 API-Manager
        ================================================================================

|  Branch | Build Status(TravisCI) |
| :------------ |:-------------
| 2.6.x      | [![Build Status](https://api.travis-ci.com/ruks/wso2-am-billing-engine.svg?branch=2.6.x)](https://travis-ci.com/ruks/wso2-am-billing-engine) |

Simple billing engine is a sample project to demonstrate the capabilities of the wso2 API-manager monetization. And also the capabilities
of extending and customization of APIM and APIM analyzer features to support billing engine.


Key Features
=============

* Ability to demonstrate APIM analytics feature to support API monetization:        
    - Integrate with a billing engine
    - Ability to extend subscription workflow to support billing
    - Data retrieval model for billing
    - Usage of analytics data for billing
    - Custom analytics rules to support billing           
    
Build the source
==================================
1. run mvn clean install to build from source
2. Billing engine web app will be available in apim-billing-engine/target
3. Workflow extension will be available in subs-billing-workflow/target

 Prerequisites
 ==================================
 
    - WSO2 API Manager 2.6.0
    - WSO2 API Manager Analytics 2.6.0
    - Billing Engine
    - Apache Tomcat 7/8
    - Mysql Database
    
 # Configure APIM and APIM analytics for analytics 
 - Refer to this document(https://docs.wso2.com/display/AM260/Configuring+APIM+Analytics) to configure Analytics
 
 # Configure APIM
 ## Deploy custom workflow extension
 
 1. Copy and paste subs-workflow-1.4.0.jar to APIM_HOME/repository/components/dropins
 2. Edit /_system/governance/apimgt/applicationdata/workflow-extensions.xml and register custom workflow extension
 ```
 <SubscriptionCreation executor="org.wso2.sample.apimgt.workflow.SubscriptionBillingWorkflow"/>
 ```
 
 ## Define billing engine configurations
 1. Edit <AM_HOME>/repository/conf/api-manager.xml and the following config
 ```
 <APIManager>
  .......
     <BillingEngine>
         <BillingEngineUrl>http://localhost:8080/apim-billing-engine-1.4.0/</BillingEngineUrl>
         <ApimStoreUrl>https://localhost:9443/store/</ApimStoreUrl>
     </BillingEngine>    
 </APIManager>
```
 
 # Configure APIM analytics
  
 1. Deploy custom siddhi app use for billing data
 2. Copy APIM_BILLING_INFO_SUMMARY.siddhi to <ANALYTICS_HOME>/wso2/worker/deployment/siddhi-files/
 
 # Configure Billing Engine
 
 1. Deploy Billing Engine
 2. Deploy apim-billing-engine-1.4.0 to tomcat container
 3. Configure <TOMCAT_HOME>/webapps/apim-billing-engine-1.4.0/WEB-INF/classes/datasource.properties as appropriate
 ```
 #datasource
 url=jdbc:mysql://localhost:3306/billing?autoReconnect=true&verifyServerCertificate=false&useSSL=false&requireSSL=false
 username=root
 password=pass
 driverClassName=com.mysql.jdbc.Driver
 dialect=org.hibernate.dialect.MySQL5InnoDBDialect
 
 #SP
 dasUrl=https://localhost:7444
 dasUserName=admin
 dasPassword=admin
 spTrustStore=/home/rukshan/apim/2.6.0/billingEngine/wso2am-analytics-2.6.0/resources/security/wso2carbon.jks
 spTrustStorePassword=wso2carbon
 
 #apim
 apimUserName=admin
 apimPassword=admin
 publisherUrl=https://localhost:9443/api/am/publisher/v0.14/apis
 tokenUrl=https://localhost:9443/oauth2/token
 dcrUrl=https://localhost:9443/client-registration/v0.14/register
 introspectUrl=https://localhost:9443/oauth2/introspect
 apimTrustStore=/home/rukshan/apim/2.6.0/billingEngine/wso2am-2.6.0/repository/resources/security/client-truststore.jks
 apimTrustStorePassword=wso2carbon
 
 #Billing engine
 adminUser=admin
 adminPass=admin
 adminRole=ROLE_ADMIN
 userRole=ROLE_USER
 ```

# Issue Tracker

Help us make our software better. Please submit any bug reports or feature
requests through GitHub:

   https://github.com/ruks/wso2-am-billing-engine/issues
   

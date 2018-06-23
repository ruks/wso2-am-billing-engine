        ================================================================================
                                        Simple Billing Engine for WSO2 API-Manager
        ================================================================================

|  Branch | Build Status(TravisCI) |
| :------------ |:-------------
| master      | [![Build Status](https://api.travis-ci.com/ruks/wso2-am-billing-engine.svg?branch=2.2.x)](https://travis-ci.com/ruks/wso2-am-billing-engine) |

Simple billing engine is a sample project to demostrace the capabilities of the wso2 API-manager monotization. And also the capabilities
of extending and cutomization of APIM ana APIM analyser features to support configure billing engine.


Key Features
=============

* Ability to demonstrate APIM analytics feature to support API monotization:        
    - Integrate with billing engine
    - Ability to extend subscription workflow to support billing
    - Data retreival datasource for billing
    - Usage of analytics data fro billing
    - Custom analytics rules to support billing           
    

System Requirements
==================================

1. WSO2 APIM 2.2.0
1. WSO2 APIM-Analytics 2.2.0
2. RDBMS (mysql 5.7, 5.6 tested)
3. Web app container (Tomcat tested)

Build the source
==================================
1. run mvn clean install to build from source
2. Billing engine web app will be available in apim-billing-engine/target
3. Workflow extension will be available in subs-billing-workflow/target

Configure APIM Analytics
==================================

1. Copy apim-billing-engine/src/main/resources/DAS-cApp/dist/APIM_Billing_2.2.0.car to 
    <AM_ANALYTICS>/repository/deployment/server/carbonapps
2. Run the wso2server.sh or wso2server.bat script based on you operating system.

Configure APIM Analytics
==============

1. Enable analytics by setting <Enabled>true</Enabled> in Analytics section of <AM_HOME>/repository/conf/api-manager.xml 
2. Open the /_system/governance/apimgt/applicationdata/workflow-extensions.xml file. Replace the SubscriptionCreation tag with following
   
   <SubscriptionCreation executor="org.wso2.sample.apimgt.workflow.SubscriptionBillingWorkflow"/>
   
3. Open <API-M home>/repository/conf/datasources/master-datasources.xml and define datasource as below
   
    <datasource>
      <name>BILLING_DB</name>
      <description>The datasource used for monetization </description>
      <jndiConfig>
          <name>jdbc/BILLING_DB</name>
      </jndiConfig>
      <definition type="RDBMS">
          <configuration>
              <url>jdbc:mysql://localhost:3306/billing?autoReconnect=true</url>
              <username>root</username>
              <password>pass</password>
              <driverClassName>com.mysql.jdbc.Driver</driverClassName>
              <maxActive>50</maxActive>
              <maxWait>60000</maxWait>
              <testOnBorrow>true</testOnBorrow>
              <validationQuery>SELECT 1</validationQuery>
              <validationInterval>30000</validationInterval>
              <defaultAutoCommit>false</defaultAutoCommit>
          </configuration>
      </definition>
    </datasource>
    
4. Copy and paste the MySQL JAR to the <AM_HOME>/repository/component/lib 
5. Copy and paste the workflow extension(subs-billing-workflow/target/subs-workflow-1.3.0.jar) to the <AM_HOME>/repository/component/lib
6. Define billingEngineUrl in <AM_HOME>/repository/conf/api-manager.xml 
<APIManager>
    ...
    <billingEngineUrl>http://localhost:8080/apim-billing-engine-1.3.0/app/main</billingEngineUrl>
    ...
</APIManager>
6. Run the wso2server.sh or wso2server.bat script based on you operating system.

Configure Billing engine
==============
1. Deploy the apim-billing-engine/target/apim-billing-engine-1.3.0.war in a tomcat server
2. Define configuration in the <tomcat_home>/webapps/apim-billing-engine-1.3.0/WEB-INF/classes/datasource.properties

    #jDBC connection url
    url=jdbc:mysql://localhost:3306/billing?autoReconnect=true&verifyServerCertificate=false&useSSL=false&requireSSL=false
    #JDBC credentials
    username=root
    password=pass
    #  JDBC driver information
    driverClassName=com.mysql.jdbc.Driver
    dialect=org.hibernate.dialect.MySQL5InnoDBDialect
    
    #Store url and credentials
    apimStoreUrl=https://localhost:9443/
    apimUserName=admin
    apimPassword=admin
    #Analyser(DAS or APIM analytics) url and credentials
    dasUrl=https://localhost:9444/
    dasUserName=admin
    dasPassword=admin
    #Path to the java key store (contain certificate for both APIM and analyser)
    jksPath=/home/rukshan/apim/2.2.0/wso2am-2.2.0/repository/resources/security/wso2carbon.jks
    

Issue Tracker
==================================

Help us make our software better. Please submit any bug reports or feature
requests through GitHub:

   https://github.com/ruks/wso2-am-billing-engine/issues

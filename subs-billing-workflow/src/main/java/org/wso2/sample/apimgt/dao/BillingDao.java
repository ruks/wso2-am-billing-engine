/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.sample.apimgt.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingDao {

    private static final Log log = LogFactory.getLog(BillingDao.class);

    private static BillingDao instance;

    private static volatile DataSource dataSource = null;

    private BillingDao () throws APIManagementException{
        createDataSource();
    }

    public synchronized static BillingDao getInstance() throws APIManagementException{
        if(instance == null){
            instance = new BillingDao();
        }
        return instance;
    }

    private void createDataSource() throws APIManagementException{

        String dataSourceName = "jdbc/BILLING_DB";

        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup(dataSourceName);
        } catch (NamingException e) {
            throw new APIManagementException("Error while looking up the data " +
                    "source: " + dataSourceName, e);
        }
    }

    public boolean userExists(String username) throws APIManagementException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String userCountSQL = "SELECT COUNT(username) AS users FROM appuser WHERE userName = ?";

        try{
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(userCountSQL);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            String count = resultSet.getString("users");
            return Integer.parseInt(count) > 0;

        } catch (SQLException e) {
            log.error("SQLException occurred while checking if user exists", e);
            throw new APIManagementException("Exception occurred while checking if user exists", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Unable to close the connection", e);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                log.error("Unable to close the Prepared Statement", e);
            }
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                log.error("Unable to close the Result Set", e);
            }
        }
    }
}

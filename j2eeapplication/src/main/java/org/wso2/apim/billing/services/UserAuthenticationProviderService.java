package org.wso2.apim.billing.services;

import org.wso2.apim.billing.domain.UserEntity;

/**
 * Provides processing service to set user authentication session
 * 
 * @author Arthur Vin
 */
public interface UserAuthenticationProviderService {

	/**
	 * Process user authentication
	 * 
	 * @param user
	 * @return
	 */
	boolean processUserAuthentication(UserEntity user);
}

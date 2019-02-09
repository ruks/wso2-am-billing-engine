package org.wso2.apim.billing.services;

import org.wso2.apim.billing.domain.InvoiceEntity;
import org.wso2.apim.billing.domain.UserEntity;

import java.util.List;

/**
 * Service providing service methods to work with user data and entity.
 *
 * @author Arthur rukshan
 */
public interface InvoiceService {

    /**
     * Create plan - persist to database
     *
     * @param user
     * @return true if success
     */
    InvoiceEntity createInvoice(UserEntity user) throws Exception;

    List<InvoiceEntity> listInvoices(UserEntity user);

    InvoiceEntity getInvoiceById(UserEntity user, long id);

    List<String> getSubscribers();
}

package org.wso2.apim.billing.ui.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.io.Serializable;

/**
 * Helper util to assist in user interface
 * 
 * @author Arthur Vin
 */
public class UIUtils implements Serializable {
	private static final long serialVersionUID = 7872083365595569634L;

	private int viewLoadCount = 0;
	
	public void greetOnViewLoad(ComponentSystemEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (viewLoadCount < 1 && !context.isPostback()) {
			String firstName = (String) event.getComponent().getAttributes().get("firstName");
			String lastName = (String) event.getComponent().getAttributes().get("lastName");
			
			FacesMessage message = new FacesMessage(String.format("Welcome to your account %s %s", firstName, lastName));
			context.addMessage("growlMessages", message);
			
			viewLoadCount++;
		}
	}

	protected FacesMessage constructErrorMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
	}

}

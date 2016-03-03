package com.example.j2eeapp.services;

import javax.faces.event.AjaxBehaviorEvent;

import com.example.j2eeapp.domain.PlanEntity;
import com.example.j2eeapp.domain.UserEntity;

import java.util.List;

/**
 * Service providing service methods to work with user data and entity.
 * 
 * @author Arthur Vin
 */
public interface PlanService {

	/**
	 * Create plan - persist to database
	 * 
	 * @param planEntity
	 * @return true if success
	 */
	boolean createPlan(PlanEntity planEntity);
	
	/**
	 * Check plan name availability. UI ajax use.
	 * 
	 * @param ajax event
	 * @return
	 */
	boolean checkAvailable(AjaxBehaviorEvent event);
	
	/**
	 * Retrieves full User record from database by plan name
	 * 
	 * @param planName
	 * @return PlanEntity
	 */
	PlanEntity loadPlanEntityByPlanName(String planName);

	List<PlanEntity> loadPlanEntities();
}

package com.example.j2eeapp.dao;

import com.example.j2eeapp.commons.dao.GenericDao;
import com.example.j2eeapp.domain.PlanEntity;
import com.example.j2eeapp.domain.UserEntity;

import java.util.List;

/**
 * Data access object interface to work with User entity database operations.
 * 
 * @author Arthur Rukshan
 */
public interface PlanDao extends GenericDao<PlanEntity, Long> {

	/**
	 * Queries database for user name availability
	 * 
	 * @param planName
	 * @return true if available
	 */
	boolean checkAvailable(String planName);
	
	/**
	 * Queries user by username
	 * 
	 * @param planName
	 * @return User entity
	 */
	PlanEntity loadPlanByPlanName(String planName);

	List<PlanEntity> loadPlans();
}

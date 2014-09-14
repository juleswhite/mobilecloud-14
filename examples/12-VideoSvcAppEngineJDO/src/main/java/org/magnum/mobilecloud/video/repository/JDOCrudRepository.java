/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mobilecloud.video.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

/**
 * This class provides a minimal interface to mimic a subset
 * of the functionality in the Spring Data Repository. This
 * example is provided solely to show how to accomplish 
 * similar types of operations using JDO. It is possible to 
 * run Spring Data on top of AppEngine's JPA implementation, 
 * which will provide an identical environment to previous
 * examples.  
 * 
 * @author jules
 *
 * @param <T> - The type of Object stored by the repository
 * @param <ID> - The type of ID used by the stored object
 */
public class JDOCrudRepository<T,ID extends Serializable> {

	private Class<T> type_;
	
	public JDOCrudRepository(Class<T> type){
		type_ = type;
	}
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	public <S extends T> S save(S entity){
		return PMF.get().getPersistenceManager().makePersistent(entity);
	}

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 */
	public <S extends T> Iterable<S> save(Iterable<S> entities){
		List<S> saved = new ArrayList<S>();
		for(S entity : entities){
			saved.add(save(entity));
		}
		return saved;
	}

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 */
	@SuppressWarnings("unchecked")
	public T findOne(ID id){
		return (T)PMF.get().getPersistenceManager().getObjectById(type_, id);
	}

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 */
	public boolean exists(ID id){
		return findOne(id) != null;
	}

	/**
	 * Returns all instances of the type.
	 * 
	 * @return all entities
	 */
	@SuppressWarnings("unchecked")
	public Iterable<T> findAll(){
		Query query = PMF.get().getPersistenceManager().newQuery(Video.class);
		Object rslt = query.execute();
		return (Collection<T>)rslt;
	}

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 */
	public void delete(ID id){
		T obj = findOne(id);
		if(obj != null){
			PMF.get().getPersistenceManager().deletePersistent(obj);
		}
	}

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 */
	public void delete(T entity){
		PMF.get().getPersistenceManager().deletePersistent(entity);
	}

}

package com.webservicemaster.iirs.dao.master;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository <T, ID extends Serializable> extends Repository<T, ID> {

	void delete( ID id );
	 
    List<T> findAll();
     
    //Optional<T> findOne(ID id);
 
    T save(T persisted);
	
}

package com.webdevelopment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.webdevelopment.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
//	@Query("SELECT u FROM User WHERE u.username =:username")
	User findByUsername(@Param("username") String username);
	
//	UserDetails findByUsername(String username);

}

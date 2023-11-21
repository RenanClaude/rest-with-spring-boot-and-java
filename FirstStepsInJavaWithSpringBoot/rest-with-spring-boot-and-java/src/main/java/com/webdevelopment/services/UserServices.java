package com.webdevelopment.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.webdevelopment.controllers.PersonController;
import com.webdevelopment.data.vo.v1.PersonVO;
import com.webdevelopment.exceptions.ResourceNotFoundException;
import com.webdevelopment.mapper.DozerMapper;
import com.webdevelopment.model.entity.User;
import com.webdevelopment.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService {

	private UserRepository repository;

	@Autowired
	public UserServices(UserRepository userRepository) {
		this.repository = userRepository;
	}

//	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(UserServices.class.getName());

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		User user = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		PersonVO personVO = DozerMapper.parseObject(user, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by username " + username + "!");

		UserDetails user = this.repository.findByUsername(username);
		if (user != null) {
//			System.out.println("AQUI: " + user);
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}
}

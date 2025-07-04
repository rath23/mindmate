package com.maq.mindmate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);


    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);
        if (user == null) {
            logger.warn("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsImpl(user);
        //we refer current user as priciple so we can also name the UserDetailsImpl as UserPrincipal
    }

}


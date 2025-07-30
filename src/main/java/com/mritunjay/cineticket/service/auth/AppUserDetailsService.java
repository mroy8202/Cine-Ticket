package com.mritunjay.cineticket.service.auth;

import com.mritunjay.cineticket.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

   UserServiceImpl userService;

   @Autowired
   AppUserDetailsService(UserServiceImpl userService) {
       this.userService = userService;
   }

   @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       return userService.getUserByUserName(userName);
   }

}

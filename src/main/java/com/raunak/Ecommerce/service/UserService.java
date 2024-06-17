package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.dto.WebUser;
import com.raunak.Ecommerce.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User findByUserName(String userName);

    void save(WebUser webUser);
}

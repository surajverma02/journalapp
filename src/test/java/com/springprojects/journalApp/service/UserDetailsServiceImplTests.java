package com.springprojects.journalApp.service;

import com.springprojects.journalApp.entity.User;
import com.springprojects.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailsServiceImplTests {

//    @InjectMocks
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceIml;

//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.initMocks(this);
//    }

    @Test
    public void loadUserByUsernameTest(){
        assertNotNull(userDetailsServiceIml.loadUserByUsername("Shyam"));
    }
}

package com.springprojects.journalApp.service;

import com.springprojects.journalApp.entity.User;
import com.springprojects.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Disabled
    @Test
    public void findByUserNameTest(){
        assertNotNull(userRepository.findByUserName("Ram"));
    }

    @Disabled
    @Test
    public void checkJournalEntriesTest(){
        User user = userRepository.findByUserName("Ram");
        assertFalse(user.getJournalEntries().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,3,5",
            "6,5,11"
    })
    public void parameterizedExample(int a, int b, int expected){
        assertEquals(expected, a+b);
    }
}

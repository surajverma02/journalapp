package com.springprojects.journalApp.service;

import com.springprojects.journalApp.entity.JournalEntry;
import com.springprojects.journalApp.entity.User;
import com.springprojects.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedJournalEntry);
            userService.saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("An error is come during saving entry, "+e);
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntry(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }

    @Transactional
    public boolean deleteEntryById(ObjectId myId, String userName){
        boolean isEntryRemoved = false;
        try {
            User user = userService.findByUserName(userName);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
            if(removed){
                userService.saveNewUser(user);
                journalEntryRepository.deleteById(myId);
            }
        }catch (Exception e){
            throw new RuntimeException("An error is come during saving entry, "+e);
        }
        return isEntryRemoved;
    }


}

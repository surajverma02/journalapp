package com.springprojects.journalApp.controller;

import com.springprojects.journalApp.entity.JournalEntry;
import com.springprojects.journalApp.entity.User;
import com.springprojects.journalApp.service.JournalEntryService;
import com.springprojects.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(!allEntries.isEmpty()){
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        // How to check an object is from a given list of objects id
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(s->s.getId().equals(myId)).toList();
        if(!journalEntries.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean isRemoved = journalEntryService.deleteEntryById(myId, userName);
        if(isRemoved){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateEntryById(@PathVariable ObjectId myId, @RequestBody JournalEntry myNewEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        // How to check an object is from a given list of objects id
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(s->s.getId().equals(myId)).toList();
        if(!journalEntries.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(myId);
            if(journalEntry.isPresent()){
                JournalEntry myOldEntry = journalEntry.get();
                myOldEntry.setTitle(!myNewEntry.getTitle().isEmpty() ? myNewEntry.getTitle() : myOldEntry.getTitle());
                myOldEntry.setContent(myNewEntry.getContent() != null && !myNewEntry.getContent().isEmpty() ? myNewEntry.getContent() : myOldEntry.getContent());
                journalEntryService.saveEntry(myOldEntry);
                return new ResponseEntity<>(myOldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

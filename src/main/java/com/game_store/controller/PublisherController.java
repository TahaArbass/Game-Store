package com.game_store.controller;

import com.game_store.model.Publisher;
import com.game_store.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        List<Publisher> publisherList = publisherService.getAllPublishers();
        return new ResponseEntity<>(publisherList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable("id") int id) {
        Optional<Publisher> publisher = publisherService.getPublisherById(id);
        return publisher.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        try {
            Publisher createdPublisher = publisherService.createPublisher(publisher);
            return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable("id") int id, @Valid @RequestBody Publisher publisher) {
        try {
            Publisher updatedPublisher = publisherService.updatePublisher(id, publisher);
            return new ResponseEntity<>(updatedPublisher, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable("id") int id) {
        publisherService.deletePublisher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/signup")
    public ResponseEntity<Publisher> signUp(@Valid @RequestBody Publisher publisher) {
        try {
            Publisher createdPublisher = publisherService.createPublisher(publisher);
            return new ResponseEntity<>(createdPublisher, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Publisher> login(@RequestBody Publisher publisher) {
        Optional<Publisher> authenticatedPublisher = publisherService.authenticatePublisher(publisher.getUsername(), publisher.getPassword());
        return authenticatedPublisher.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Publisher> getPublisherByEmail(@PathVariable("email") String email) {
        Optional<Publisher> publisher = publisherService.getPublisherByEmail(email);
        return publisher.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Publisher> getPublisherByUsername(@PathVariable("username") String username) {
        Optional<Publisher> publisher = publisherService.getPublisherByUsername(username);
        return publisher.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}

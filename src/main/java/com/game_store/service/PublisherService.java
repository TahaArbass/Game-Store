package com.game_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.game_store.model.Publisher;
import com.game_store.repository.PublisherRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> getPublisherById(int id) {
        return publisherRepository.findById(id);
    }

    public Publisher createPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(int id, Publisher publisher) {
        if (publisherRepository.existsById(id)) {
            publisher.setPublisherID(id);
            return publisherRepository.save(publisher);
        } else {
            throw new IllegalArgumentException("Publisher with ID " + id + " not found");
        }
    }

    public void deletePublisher(int id) {
        publisherRepository.deleteById(id);
    }

    public Optional<Publisher> authenticatePublisher(String username, String password) {
        return publisherRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<Publisher> getPublisherByEmail(String email) {
        return publisherRepository.findByEmail(email);
    }

    public Optional<Publisher> getPublisherByUsername(String username) {
        return publisherRepository.findByUsername(username);
    }
}

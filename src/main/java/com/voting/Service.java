package com.voting;

import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service
public class Service {
    private final ItemRepo repo;

    public Service(ItemRepo repo) {
        this.repo = repo;
    }

    public List<Item> findAll() {
        return repo.findAll();
    }

    public Item save(Item item) {
        return repo.save(item);
    }

    public Optional<Item> findById(Long id) {
        return repo.findById(id);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

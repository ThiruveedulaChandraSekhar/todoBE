package com.voting;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class AppController {

    private final Service service;

    public AppController(Service service) {
        this.service = service;
    }

    // read allowed origin from application.properties (fallback to localhost:5173)
    @Value("${allowed.origin:http://localhost:5173}")
    private String allowedOrigin;

    @CrossOrigin(origins = "${allowed.origin:http://localhost:5173}")
    @GetMapping
    public List<Item> getAll() {
        return service.findAll();
    }

    @CrossOrigin(origins = "${allowed.origin:http://localhost:5173}")
    @PostMapping
    public ResponseEntity<Item> create(@RequestBody Item incoming) {
        // sanitize a little
        if (incoming.getItem() == null || incoming.getItem().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Item saved = service.save(new Item(incoming.getItem().trim()));
        return ResponseEntity.created(URI.create("/api/items/" + saved.getId())).body(saved);
    }

    @CrossOrigin(origins = "${allowed.origin:http://localhost:5173}")
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Item> toggle(@PathVariable Long id) {
        return service.findById(id).map(item -> {
            item.setCompleted(!item.isCompleted());
            return ResponseEntity.ok(service.save(item));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "${allowed.origin:http://localhost:5173}")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

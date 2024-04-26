package com.samuelnunes.todosimple.controllers;

import com.samuelnunes.todosimple.models.User;
import com.samuelnunes.todosimple.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = userService.findById(id);
        return ResponseEntity.ok().body(obj);
    }
    @GetMapping
    public ResponseEntity<List<User>>findAll(){
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    @Validated(User.CreateUser.class)
    public ResponseEntity<User> createUser(@RequestBody @Valid User obj) {
        userService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }
    @PutMapping("/{id}")
    @Validated(User.UpdateUser.class)
    public ResponseEntity<User> updateUser(@RequestBody @Valid User obj, @PathVariable Long id) {
        obj.setId(id);
        userService.update(obj);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

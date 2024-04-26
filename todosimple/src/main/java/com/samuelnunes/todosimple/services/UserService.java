package com.samuelnunes.todosimple.services;

import com.samuelnunes.todosimple.models.User;
import com.samuelnunes.todosimple.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(()->new RuntimeException(
                "User with id " + id + " not found, type: " + User.class.getName()
        ));
    }

    @Transactional
    public User create(User user){
        user.setId(null);
        user = this.userRepository.save(user);
        return user;
    }

    @Transactional
    public User update(User obj){
        User newUser = findById(obj.getId());
        newUser.setPassword(obj.getPassword());
        return this.userRepository.save(newUser);
    }

    public void delete(Long id){
        try {
          this.userRepository.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("Cannot delete as there are related entities");
        }
    }
}

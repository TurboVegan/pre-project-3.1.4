package org.example.SecurityApp.services;

import org.example.SecurityApp.models.User;
import org.example.SecurityApp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> foundPerson = usersRepository.findById(id);

        return foundPerson.orElse(null);
    }

    public User findByUsername(String username) {
        Optional<User> foundPerson = usersRepository.findByUsername(username);

        return foundPerson.orElse(null);
    }

    @javax.transaction.Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    @Transactional
    public void update(int id, User updatedPerson) {
        updatedPerson.setId(id);
        updatedPerson.setPassword(passwordEncoder.encode(updatedPerson.getPassword()));
        usersRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        usersRepository.deleteById(id);
    }
}

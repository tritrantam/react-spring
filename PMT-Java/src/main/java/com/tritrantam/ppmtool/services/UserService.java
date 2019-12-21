package com.tritrantam.ppmtool.services;

import com.tritrantam.ppmtool.domain.User;
import com.tritrantam.ppmtool.exceptions.SameEmailException;
import com.tritrantam.ppmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));

            //username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            //make sure that password and confirm password match
            //don't persist or show the confirm password
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new SameEmailException("Username" + newUser.getUsername()+ " already exist");
        }
    }
}

package com.userregistration.service;

import com.userregistration.dto.UserLookupDTO;
import com.userregistration.exception.DuplicateAadhaarException;
import com.userregistration.exception.UserNotFoundException;
import com.userregistration.model.User;
import com.userregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public User createUser(User user) {
        // Check if Aadhaar already exists
        if (userRepository.existsByAadhaarNumber(user.getAadhaarNumber())) {
            throw new DuplicateAadhaarException("Aadhaar number already registered");
        }
        
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public User findUserByAadhaarAndDob(UserLookupDTO lookupDTO) {
        return userRepository.findByAadhaarNumberAndDateOfBirth(
                lookupDTO.getAadhaarNumber(), 
                lookupDTO.getDateOfBirth())
            .orElseThrow(() -> new UserNotFoundException("User not found or Aadhaar and Date of Birth don't match"));
    }
}
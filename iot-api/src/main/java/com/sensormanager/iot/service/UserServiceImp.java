package com.sensormanager.iot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.UserDataAdapter;
import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.CustomUserSecurity;
import com.sensormanager.iot.model.User;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		List<User> users = userRepository.findAll();
                return users.stream().map(UserDataAdapter::toDTO).collect(Collectors.toList());
        	} else {
        		List<User> users = userRepository.findByCompany(userAuth.getCompany());
                return users.stream().map(UserDataAdapter::toDTO).collect(Collectors.toList());
        	}
        } else return new ArrayList<UserDTO>();    
    }

    @Override
    public UserDTO findById(Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		User user = userRepository.findById(id).orElse(null);
                if (user == null || user.getId() == null) return new UserDTO();
                return UserDataAdapter.toDTO(user);
        	} else {
        		User user = userRepository.findByIdAndCompany(id, userAuth.getCompany()).orElse(null);
        		if (user == null || user.getId() == null) return new UserDTO();
                return UserDataAdapter.toDTO(user);
        	}
        } else return new UserDTO();        
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
    	Company company = new Company();
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) company = companyRepository.findById(userDTO.getCompanyId()).orElse(null);        		
        	else company = companyRepository.findById(userAuth.getCompany().getId()).orElse(null);
            if (company == null) return new UserDTO();        	
        } else return new UserDTO();
        
        User user = UserDataAdapter.toEntity(userDTO);
        user.setCompany(company);
        user.setUserStatus(true);
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);
        
        User userSaved = userRepository.save(user);
        return UserDataAdapter.toDTO(userSaved);
    }

    @Override
    public UserDTO update(UserDTO userDto) {
        User userToUpdate = userRepository.findById(userDto.getId()).orElse(null);
        if (userToUpdate == null) return new UserDTO();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (!userAuth.hasRole("ROOT") && userToUpdate.getCompany().getId() != userAuth.getCompany().getId()) return new UserDTO();
        }
        
        userToUpdate.setFirstName(userDto.getFirstName() != null && userDto.getFirstName().length() > 0 ? userDto.getFirstName() : userToUpdate.getFirstName());
        userToUpdate.setLastName(userDto.getLastName() != null && userDto.getLastName().length() > 0 ? userDto.getLastName() : userToUpdate.getLastName());
        userToUpdate.setUsername(userDto.getUsername() != null && userDto.getUsername().length() > 0 ? userDto.getUsername() : userToUpdate.getUsername());
        if(userDto.getPassword() != null && userDto.getPassword().length() > 0) {
	        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
	        userToUpdate.setPassword(encodedPassword);
        }
        userToUpdate.setUserEmail(userDto.getUserEmail() != null && userDto.getUserEmail().length() > 0 ? userDto.getUserEmail() : userToUpdate.getUserEmail());
        User userUpdated = userRepository.save(userToUpdate);
        return UserDataAdapter.toDTO(userUpdated);
    }

    @Override
    public UserDTO deleteById(Long id) {
        User userDelete = userRepository.findById(id).orElse(null);
        if (userDelete != null) {        	
        	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
            	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
            	if (!userAuth.hasRole("ROOT") && userAuth.getCompany().getId() != userAuth.getCompany().getId()) return new UserDTO();
            } else return new UserDTO();
            
            userDelete.setUserStatus(false);
            userRepository.save(userDelete);
        }
        return UserDataAdapter.toDTO(userDelete);
    }
}

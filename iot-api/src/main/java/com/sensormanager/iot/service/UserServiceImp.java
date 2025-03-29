package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.adapter.UserDataAdapter;
import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.User;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id);
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The User ID: " + id + " does not exist."
            );
        }
        return UserDataAdapter.toDTO(user);
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        Company company = companyRepository.findById(userDTO.getCompanyId()).orElse(null);
        Company companyName = companyRepository.findById(userDTO.getCompanyId()).orElse(null);
        if (company == null) {
            return new UserDTO();
        }
        User user = UserDataAdapter.toEntity(userDTO);
        user.setCompany(company);
        user.setCompanyName(companyName);
        user.setUserStatus(true);
        User userSaved = userRepository.save(user);
        return UserDataAdapter.toDTO(userSaved);
    }

    @Override
    public UserDTO update(UserDTO userDto) {
        User userToUpdate = userRepository.findById(userDto.getId());
        if (userToUpdate == null) {
            return new UserDTO();
        }
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setPassword(userDto.getPassword());
        userToUpdate.setUserEmail(userDto.getUserEmail());
        User userUpdated = userRepository.save(userToUpdate);
        return UserDataAdapter.toDTO(userUpdated);
    }

    @Override
    public UserDTO deleteById(Long id) {
        User userDelete = userRepository.findById(id);
        if (userDelete != null) {
            userRepository.delete(userDelete);
        }
        return UserDataAdapter.toDTO(userDelete);
    }
}

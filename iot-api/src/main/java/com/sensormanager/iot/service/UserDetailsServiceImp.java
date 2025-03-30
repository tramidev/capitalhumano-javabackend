package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.model.User;
import com.sensormanager.iot.repository.UserRepository;
import com.sensormanager.iot.security.CustomUserSecurity;

@Service
public class UserDetailsServiceImp implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario o contraseña son incorrectos."));
		if (!user.hasRole("ROOT") && (user.getCompany() == null || !user.getCompany().getCompanyStatus())) throw new UsernameNotFoundException("La compañía asociada al usuario está deshabilitada.");
		List<SimpleGrantedAuthority> authorities = user.getRoleName().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
		return new CustomUserSecurity(user.getUsername(), user.getPassword(), user.getUserStatus(), authorities, user.getCompany());
	}

}

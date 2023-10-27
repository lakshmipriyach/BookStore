package com.bookstore.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Login;
import com.bookstore.entity.Role;
import com.bookstore.repository.LoginRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private LoginRepository loginRepository;

	public CustomUserDetailsService(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrUserId) throws UsernameNotFoundException {
		Login user = loginRepository.findByUserId(usernameOrUserId);

		if (user == null) {
			// If not found by userId, try finding by userName
			user = loginRepository.findByUserName(usernameOrUserId);
		}

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username or userId: " + usernameOrUserId);
		}

		// Proceed with creating UserDetails and returning it.

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}

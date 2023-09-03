package com.elearning.app;

import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserRepository;
import com.elearning.app.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAccount> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        UserAccount user = userOpt.get();

        Set<UserRole> roles = user.getRoles();
        for (UserRole role : roles) {
            authorityList.add(new SimpleGrantedAuthority(role.name()));
        }
        return new User(user.getEmail(), user.getPassword(), authorityList);
    }
}
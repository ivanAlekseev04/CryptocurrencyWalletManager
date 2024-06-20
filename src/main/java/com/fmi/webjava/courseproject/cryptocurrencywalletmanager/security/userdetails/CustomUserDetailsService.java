package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.security.userdetails;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Data
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        var user = userRepository.findByUserName(userName);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User with name '%s' not found", userName));
        }

        return CustomUserDetails.builder()
                .id(user.get().getId())
                .userName(user.get().getUserName())
                .password(user.get().getPassword())
                .authorities(Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_USER")))
                .build();
//        return new org.springframework.security.core.userdetails.User(
//                user.get().getUserName(),
//                user.get().getPassword(),
//                true,
//                true,
//                true,
//                true,
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//        );
    }
}

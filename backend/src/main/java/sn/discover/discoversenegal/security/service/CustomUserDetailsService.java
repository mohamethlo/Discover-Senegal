package sn.discover.discoversenegal.security.service;

import lombok.RequiredArgsConstructor;
import sn.discover.discoversenegal.entities.User;
import sn.discover.discoversenegal.repositories.UserRepository;
import sn.discover.discoversenegal.security.CustomUserDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
        return new CustomUserDetails(user);
    }
}

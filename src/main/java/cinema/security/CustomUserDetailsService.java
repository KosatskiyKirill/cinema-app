package cinema.security;

import cinema.model.User;
import cinema.service.UserService;
import java.util.Optional;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = userService.findByEmail(email);
        UserBuilder builder;
        if (optionalUser.isPresent()) {
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.password(optionalUser.get().getPassword());
            builder.roles(optionalUser.get().getRoles()
                    .stream()
                    .map(r -> r.getRoleName().name())
                    .toArray(String[]::new));
            return builder.build();
        }
        throw new UsernameNotFoundException("Can't found user with email: " + email);
    }
}

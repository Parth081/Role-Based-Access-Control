import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    
    import java.util.HashSet;
    import java.util.Optional;
    import java.util.Set;
    
    @Service
    public class UserService {
        @Autowired
        private UserRepository userRepository;
    
        @Autowired
        private RoleRepository roleRepository;
    
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;
    
        @Transactional
        public void registerUser (User user) {
            // Check if the username already exists
            if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }
    
            // Encode the password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
    
            // Assign default role
            Role userRole = roleRepository.findByName("USER");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
    
            // Save the user
            userRepository.save(user);
        }
    
        public Optional<User> findByUsername(String username) {
            return Optional.ofNullable(userRepository.findByUsername(username));
        }
    }
    private String SECRET_KEY = "secret";

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssued
package com.elearning.app;

import com.elearning.app.user.UserAccount;
import com.elearning.app.user.UserAccountRequest;
import com.elearning.app.user.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    protected final Log logger = LogFactory.getLog(getClass());

    final UserRepository userRepository;
    final AuthenticationManager authenticationManager;
    final JwtUserDetailsService userDetailsService;
    final JwtTokenUtil jwtTokenUtil;

    public AuthController(UserRepository userRepository, AuthenticationManager authenticationManager,
                          JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserAccountRequest user) {
        Map<String, Object> responseMap = new HashMap<>();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        UserAccount userAccount = userRepository.findByEmail(user.getEmail()).get();
        if (userAccount.isDisabledAccount()) {
            responseMap.put("error", true);
            responseMap.put("message", "Account disabled");
            return ResponseEntity.status(401).body(responseMap);
        }

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");

                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                responseMap.put("role", userDetails.getAuthorities());
                responseMap.put("email", user.getEmail());
                responseMap.put("expiresIn", jwtTokenUtil.getExpirationDateFromToken(token));
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserAccountRequest user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().noneMatch(e -> e.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(400).body("Email unique!");
        }

        String password = generatePassword();
        Map<String, Object> responseMap = new HashMap<>();
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        UserAccount userAccount = getUser(user);
        String token = jwtTokenUtil.generateToken(userAccount);
        userRepository.save(userAccount);
        responseMap.put("error", false);
        responseMap.put("email", user.getEmail());
        responseMap.put("password", password);
        responseMap.put("message", "Account created successfully");
        responseMap.put("token", token);
        return ResponseEntity.ok(responseMap);
    }

    private String generatePassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private UserAccount getUser(UserAccountRequest user) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(user.getEmail());
        userAccount.setFirstName(user.getFirstName());
        userAccount.setLastName(user.getLastName());
        userAccount.setPassword(user.getPassword());
        userAccount.setRoles(user.getRoles());
        return userAccount;
    }
}
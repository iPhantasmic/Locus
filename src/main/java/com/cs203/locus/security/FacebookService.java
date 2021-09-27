package com.cs203.locus.security;

import com.cs203.locus.models.security.FacebookUser;
import com.cs203.locus.models.security.JwtResponse;
import com.cs203.locus.models.user.User;
import com.cs203.locus.models.user.UserDTO;
import com.cs203.locus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FacebookService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacebookClient facebookClient;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    public JwtResponse loginUser(String fbAccessToken) {
        FacebookUser facebookUser = facebookClient.getUser(fbAccessToken);

        User toLogin = null;
        if (userRepository.findById(facebookUser.getId()).isEmpty()) {
            toLogin = createNormalUser(facebookUser);
        } else {
            userRepository.findById(facebookUser.getId()).get();
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(toLogin.getUsername());
        final String token = jwtTokenUtil.generateAuthToken(userDetails);
        final String username = toLogin.getUsername();
        final String email = toLogin.getEmail();

        // returns JSON object containing username, email and JWT token of logged in Facebook user
        return new JwtResponse(username, email, token);
    }

    private User createNormalUser(FacebookUser facebookUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(generateUsername(facebookUser.getFirstName(), facebookUser.getLastName()));
        userDTO.setPassword(generatePassword(16));
        userDTO.setEmail(facebookUser.getEmail());
        userDTO.setName(facebookUser.getFirstName());
        userDTO.setConfirmPassword(userDTO.getPassword());
        User newUser = jwtUserDetailsService.create(userDTO);
//        newUser.setRole("ROLE_FACEBOOK_USER"); TODO: test if this is necessary

        return newUser;
    }

    private String generateUsername(String firstName, String lastName) {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%s.%s.%06d", firstName, lastName, number);
    }

    private String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return new String(password);
    }
}
package com.cs203.locus.security;

import com.cs203.locus.models.organiser.Organiser;
import com.cs203.locus.models.participant.Participant;
import com.cs203.locus.models.security.GoogleUser;
import com.cs203.locus.models.security.JwtResponse;
import com.cs203.locus.models.user.User;
import com.cs203.locus.models.user.UserDTO;
import com.cs203.locus.repository.UserRepository;
import com.cs203.locus.service.OrganiserService;
import com.cs203.locus.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Random;

@Service
public class GoogleService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoogleClient googleClient;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private OrganiserService organiserService;

    public Object[] loginUser(String googleAccessToken) {
        GoogleUser googleUser;
        try {
            googleUser = googleClient.getUser(googleAccessToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
        }

        User toLogin = null;
        if (userRepository.findByEmail(googleUser.getEmail()) == null) {
            toLogin = createNormalUser(googleUser);
        } else {
            toLogin = userRepository.findByEmail(googleUser.getEmail());
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(toLogin.getUsername());
        final String token = jwtTokenUtil.generateAuthToken(userDetails);

        // returns JSON object containing username, email and JWT token of logged in Facebook user
        return new Object[]{new JwtResponse(toLogin.getId(), toLogin.getName(), toLogin.getUsername(), token), token};
    }

    private User createNormalUser(GoogleUser googleUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(generateUsername(googleUser.getFirstName(), googleUser.getLastName()));
        userDTO.setPassword(generatePassword(16));
        userDTO.setEmail(googleUser.getEmail());
        userDTO.setName(googleUser.getFirstName());
        userDTO.setConfirmPassword(userDTO.getPassword());
        User newUser = jwtUserDetailsService.create(userDTO);

        Participant newParticipant = new Participant();
        newParticipant.setId(newUser.getId());
        newParticipant.setVaxStatus(false);
        newParticipant.setUser(newUser);
        newParticipant.setEventTicket(new ArrayList<>());
        participantService.createParticipant(newParticipant);

        Organiser newOrganiser = new Organiser();
        newOrganiser.setId(newUser.getId());
        newOrganiser.setUser(newUser);
        newOrganiser.setEvents(new ArrayList<>());
        organiserService.createOrganiser(newOrganiser);

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

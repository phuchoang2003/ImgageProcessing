package com.example.dev.version1.auth;


import com.example.dev.version1.security.JwtService;
import com.example.dev.version1.exceptionHandling.InputInvalidException;
import com.example.dev.version1.user.Role;
import com.example.dev.version1.user.UserRepository;
import com.example.dev.version1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse login(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse registerNewUserAccount(RegisterRequest request) {

        String email = request.getEmail();
        String userName = request.getUserName();
        String password = request.getPassword();
        String confirmPassword = request.getConfirmPassword();

        // check email exists
        // check user name exists
        //check password > 8 and < 32, must have contains at least one unique character
        //check password == passwordConfirm
        try{
            isAccountValid(userName,email,password,confirmPassword);
        }
        catch (RuntimeException exception){
            throw new RuntimeException(exception);
        }

        // encode password before save in database
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    private boolean isEmailExisted(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isUserNameExisted(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }



    // Use regex to validate
    private boolean isPasswordContainsUniqueCharacter(String password) {
        // regex contains special character
        String regex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";
        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(password);

        // Check if a special character is found in the password
        return matcher.find();
    }

    private boolean isPasswordInRangeValid(String password) {
        int len = password.length();
        if (len >= 8 && len <= 32) return true;
        return false;
    }


    private boolean isPasswordEqualsConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private boolean isValidFormatEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void isAccountValid(String userName, String email, String password, String confirmPassword) {
        if(!isValidFormatEmail(email)){
            throw new InputInvalidException("Email must have contains @");
        }
        if (isEmailExisted(email)) {
            throw new InputInvalidException("There is an account with that email address: " + email);
        }

        if(isUserNameExisted(userName)) {
            throw new InputInvalidException("There is an account with that user name: " + userName);
        }

        if(!isPasswordInRangeValid(password)) {
            throw new InputInvalidException("Password must have in range 8 to 32 character");
        }

        if(!isPasswordContainsUniqueCharacter(password)) {
            throw new InputInvalidException("Password must have contains at least one special character");
        }

        if(!isPasswordEqualsConfirmPassword(password, confirmPassword)) {
            throw new InputInvalidException("Password and confirm password are not equal");
        }


    }
}

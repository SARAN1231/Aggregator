package com.saran.Agreegator.Services.Impl;

import com.saran.Agreegator.Dtos.LoginRequest;
import com.saran.Agreegator.Dtos.RegisterRequest;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.UserDto;
import com.saran.Agreegator.Exceptions.InvalidCredentialsException;
import com.saran.Agreegator.Exceptions.NotFoundException;
import com.saran.Agreegator.Models.User;
import com.saran.Agreegator.Repository.UserRepository;
import com.saran.Agreegator.Security.JWTUtils;
import com.saran.Agreegator.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final ModelMapper modelMapper;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // 12 -> 12 times it is hashed


    @Override
    public Response RegisterUser(RegisterRequest registerRequest) {
        log.info("Registering user: {}", registerRequest.getEmail());

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Email already exists")
                    .build();
        }

        User user = modelMapper.map(registerRequest, User.class); // converting registerReq to User class
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .user(modelMapper.map(user, UserDto.class))
                .build();

    }

    @Override
    public Response LoginUser(LoginRequest loginRequest) {

        log.info("Logging in user: {}", loginRequest.getEmail());


        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("Email not Found"));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("password does not match");
        }
        String token = jwtUtils.generateToken(loginRequest.getEmail());
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .expiryDate("6 Month")
                .build();
    }

    @Override
    public Response getAllUsers() {
        log.info("Fetching all users");

        List<UserDto> users = userRepository.findAll().stream()
                .map(user-> modelMapper.map(user,UserDto.class))
                .toList();

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Users fetched successfully")
                .users(users)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();// Fetch email from token
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Email not Found"));
    }

    @Override
    public Response UpdateUser(Long id, UserDto userDto) {

        log.info("Updating user with ID: {}", id);

        Optional<User> useroptional = userRepository.findById(id);

        if(useroptional.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("User not found")
                    .build();
        }

        User user = useroptional.get();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(user);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully")
                .user(modelMapper.map(user, UserDto.class))
                .build();
    }

    @Override
    public Response DeleteUser(Long id) {

        log.info("Deleting user with ID: {}", id);
        if(!userRepository.existsById(id)) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("User not found")
                    .build();
        }
        userRepository.deleteById(id);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
    }
}

package com.saran.Agreegator.Controllers;

import com.saran.Agreegator.Dtos.LoginRequest;
import com.saran.Agreegator.Dtos.RegisterRequest;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")                 // valid -> to validate the fields
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        Response response = userService.RegisterUser(registerRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Response response = userService.LoginUser(loginRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

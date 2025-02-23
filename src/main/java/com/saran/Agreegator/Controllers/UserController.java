package com.saran.Agreegator.Controllers;

import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.UserDto;
import com.saran.Agreegator.Models.User;
import com.saran.Agreegator.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentLoggedInUser();
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        Response response = userService.UpdateUser(id, userDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = userService.DeleteUser(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

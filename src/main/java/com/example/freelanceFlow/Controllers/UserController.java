package com.example.freelanceFlow.Controllers;

import com.example.freelanceFlow.DTO.JwtRequest;
import com.example.freelanceFlow.DTO.RegistrationUserDto;
import com.example.freelanceFlow.Models.User;
import com.example.freelanceFlow.Services.AuthService;
import com.example.freelanceFlow.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody User userForm) {
        User existingUser = userService.findByEmail(userForm.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с такой почтой уже существует");
        }
        userService.saveUser(userForm);
				return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @GetMapping("/{id}")
		public ResponseEntity<User> getUserById(@PathVariable Long id) {
				return userService.getUserById(id)
								.map(ResponseEntity::ok)
								.orElse(ResponseEntity.notFound().build());
		}


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

		@PutMapping("/{id}")
		public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody User user) {
				Optional<User> existingUser = userService.getUserById(id);
				if (existingUser.isPresent()) {
						user.setId(id);
						userService.saveUser(user);
						return ResponseEntity.ok().build();
				} else {
						return ResponseEntity.notFound().build();
				}
		}



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        User currentUser = authService.getCurrentUser(token);
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

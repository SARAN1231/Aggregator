package com.saran.Agreegator.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saran.Agreegator.Enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class UserDto {

    private Long id;


    private String username;

    private String email;

    @JsonIgnore // ignoring Pass
    private String password;

    private String phoneNumber;

    private UserRole role;

    private LocalDateTime createdAt = LocalDateTime.now();
}

package com.saran.Agreegator.Services;

import com.saran.Agreegator.Dtos.LoginRequest;
import com.saran.Agreegator.Dtos.RegisterRequest;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.UserDto;
import com.saran.Agreegator.Models.User;

public interface UserService {

     Response RegisterUser(RegisterRequest registerRequest);
     Response LoginUser(LoginRequest loginRequest);
     Response getAllUsers();
     User getCurrentLoggedInUser();
     Response UpdateUser(Long id, UserDto userDto);
     Response DeleteUser(Long id);
}

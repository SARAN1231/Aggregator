package com.saran.Agreegator.Services;

import com.saran.Agreegator.Dtos.LoginRequest;
import com.saran.Agreegator.Dtos.RegisterRequest;
import com.saran.Agreegator.Dtos.Response;
import com.saran.Agreegator.Dtos.UserDto;
import com.saran.Agreegator.Models.User;

public interface UserService {

    public Response RegisterUser(RegisterRequest registerRequest);
    public Response LoginUser(LoginRequest loginRequest);
    public Response getAllUsers();
    public User getCurrentLoggedInUser();
    public Response UpdateUser(Long id, UserDto userDto);
    public Response DeleteUser(Long id);
}

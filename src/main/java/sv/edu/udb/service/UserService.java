package sv.edu.udb.service;

import sv.edu.udb.auth.dto.request.AuthRequest;
import sv.edu.udb.auth.dto.request.RegisterRequest;
import sv.edu.udb.auth.dto.response.UserDto;
import sv.edu.udb.auth.dto.response.AuthResponse;

import java.util.List;

public interface UserService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
package lk.ijse.backend.service;

import lk.ijse.backend.dto.AuthRequestDTO;
import lk.ijse.backend.dto.AuthResponseDTO;
import lk.ijse.backend.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO authRequest);
    AuthResponseDTO register(RegisterRequestDTO registerRequest);
}

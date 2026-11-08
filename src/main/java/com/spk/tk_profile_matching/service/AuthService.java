package com.spk.tk_profile_matching.service;

import com.spk.tk_profile_matching.dto.LoginRequestDTO;
import com.spk.tk_profile_matching.dto.LoginResponseDTO;
import com.spk.tk_profile_matching.model.Users;
import com.spk.tk_profile_matching.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Optional<Users> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Username atau password salah!");
        }

        Users user = userOptional.get();

        // 1. Cek apakah akun sedang terkunci / nonaktif
        if (Boolean.TRUE.equals(user.getIsLocked())) {
            throw new RuntimeException("Akun Anda telah dinonaktifkan karena gagal login lebih dari 3 kali. Hubungi Admin!");
        }

        // 2. Cek Password (Sederhana untuk demo, sebaiknya pakai BCryptPasswordEncoder untuk produksi)
        if (!user.getPassword().equals(request.getPassword())) {
            int attempts = user.getFailedAttempt() + 1;
            user.setFailedAttempt(attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setIsLocked(true); // Nonaktifkan akun
                userRepository.save(user);
                throw new RuntimeException("Akun Anda telah dinonaktifkan karena telah gagal login 3 kali!");
            }

            userRepository.save(user);
            int sisa = MAX_FAILED_ATTEMPTS - attempts;
            throw new RuntimeException("Username atau password salah! Sisa percobaan: " + sisa);
        }

        // 3. Jika login berhasil, reset counter percobaan gagal
        user.setFailedAttempt(0);
        userRepository.save(user);

        return LoginResponseDTO.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login Berhasil")
                .build();
    }
}
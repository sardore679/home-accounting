package home_accounting.service;


import home_accounting.dto.request.LoginRequestDTO;
import home_accounting.dto.request.RegisterRequestDTO;
import home_accounting.dto.response.AuthResponseDTO;
import home_accounting.entity.User;
import home_accounting.repository.UserRepository;
import home_accounting.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Bu username band: " + request.username());
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Bu email band: " + request.email());
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getUsername());

        return AuthResponseDTO.of(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi: " + request.username()));

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponseDTO.of(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

    }

}

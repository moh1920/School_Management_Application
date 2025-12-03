package test_Technique_stage.test_Technique_stage.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import test_Technique_stage.test_Technique_stage.DTOs.AuthRequest;
import test_Technique_stage.test_Technique_stage.DTOs.AuthResponse;
import test_Technique_stage.test_Technique_stage.DTOs.RegisterRequest;
import test_Technique_stage.test_Technique_stage.entity.Admin;
import test_Technique_stage.test_Technique_stage.security.JwtService;
import test_Technique_stage.test_Technique_stage.security.LoginAttemptService;
import test_Technique_stage.test_Technique_stage.service.AdminService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final LoginAttemptService loginAttemptService;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
//            );
//            String token = jwtService.generateToken(req.getUsername());
//            return ResponseEntity.ok(new AuthResponse(token));
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(401).body("Identifiants invalides");
//        }
//    }




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        String username = req.getUsername();

        // Vérifier si le compte est bloqué
        if (loginAttemptService.isBlocked(username)) {
            return ResponseEntity.status(429).body("Trop de tentatives. Réessayez plus tard.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, req.getPassword())
            );

            // Succès → réinitialiser les tentatives
            loginAttemptService.loginSucceeded(username);

            String token = jwtService.generateToken(username);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            // Échec → incrémenter les tentatives
            loginAttemptService.loginFailed(username);
            return ResponseEntity.status(401).body("Identifiants invalides");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (adminService.existsByUsername(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username déjà utilisé");
        }
        Admin admin = new Admin();
        admin.setUsername(req.getUsername());
        admin.setPassword(passwordEncoder.encode(req.getPassword()));
        adminService.save(admin);
        return ResponseEntity.ok().build();
    }


}

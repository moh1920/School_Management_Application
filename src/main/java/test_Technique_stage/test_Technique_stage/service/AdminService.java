package test_Technique_stage.test_Technique_stage.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import test_Technique_stage.test_Technique_stage.entity.Admin;
import test_Technique_stage.test_Technique_stage.repositories.AdminRepo;

import java.util.List;

@Service // <-- rend ce bean détectable par Spring
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepo adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found: " + username));

        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public boolean existsByUsername(String username) {
        return adminRepository.existsByUsername(username);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }
}

package ra.java_service_15.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.java_service_15.model.dto.request.UpdateRoleRequest;
import ra.java_service_15.model.dto.request.UserLogin;
import ra.java_service_15.model.dto.request.UserRegister;
import ra.java_service_15.model.dto.response.JWTResponse;
import ra.java_service_15.model.dto.response.UserInfoResponse;
import ra.java_service_15.model.entity.Role;
import ra.java_service_15.model.entity.User;
import ra.java_service_15.repository.RoleRepository;
import ra.java_service_15.repository.UserRepository;
import ra.java_service_15.security.jwt.JWTProvider;
import ra.java_service_15.security.principal.CustomUserDetails;
import ra.java_service_15.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public User register(UserRegister userRegister) {
        User user = User.builder()
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .fullName(userRegister.getFullName())
                .address(userRegister.getAddress())
                .email(userRegister.getEmail())
                .phone(userRegister.getPhone())
                .roles(mapRoleStringToRole(userRegister.getRoles()))
                .enabled(userRegister.isEnabled())
                .build();
        return userRepository.save(user);
    }

    @Override
    public JWTResponse login(UserLogin userLogin) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        }catch (AuthenticationException e){
            log.error(e.getMessage());
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userDetails.getUsername());

        return JWTResponse.builder()
                .username(userDetails.getUsername())
                .fullName(userDetails.getFullName())
                .enabled(userDetails.isEnabled())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .authorities(userDetails.getAuthorities())
                .token(token)
                .build();
    }

    @Override
    public UserInfoResponse getCurrentUserInfo(String username) {
        User user = userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        return mapToUserInfoResponse(user);
    }

    @Override
    @Transactional
    public UserInfoResponse updateUserRole(Long userId, UpdateRoleRequest request) {
        // Tìm user cần cập nhật quyền
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Tìm role mới
        Role newRole = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại: " + request.getRoleName()));

        // Clear all existing roles và add role mới (hoặc có thể add thêm tùy business logic)
        user.getRoles().clear();
        user.getRoles().add(newRole);

        User updatedUser = userRepository.save(user);

        return mapToUserInfoResponse(updatedUser);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }


    private List<Role> mapRoleStringToRole(List<String> roles) {
        List<Role> roleList = new ArrayList<>();

        if(roles!=null && !roles.isEmpty()){
            roles.forEach(role->{
                switch (role){
                    case "ROLE_ADMIN":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(()-> new NoSuchElementException("Khong ton tai role_admin")));
                        break;
                    case "ROLE_USER":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(()-> new NoSuchElementException("Khong ton tai role_user")));
                        break;
                    case "ROLE_MODERATOR":
                        roleList.add(roleRepository.findByRoleName(role).orElseThrow(()-> new NoSuchElementException("Khong ton tai role_moderator")));
                        break;
                    default:
                        roleList.add(roleRepository.findByRoleName("ROLE_USER").orElseThrow(()-> new NoSuchElementException("Khong ton tai role_user")));
                }
            });
        }else{
            roleList.add(roleRepository.findByRoleName("ROLE_USER").orElseThrow(()-> new NoSuchElementException("Khong ton tai role_user")));
        }
        return roleList;
    }
    private UserInfoResponse mapToUserInfoResponse(User user) {
        // Convert List<Role> thành List<String>
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .toList();

        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roleNames(roleNames) // List<String> thay vì String
                .enabled(user.isEnabled()) // boolean thay vì Boolean status
                .build();
    }
}

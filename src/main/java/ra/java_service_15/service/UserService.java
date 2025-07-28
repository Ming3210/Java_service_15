package ra.java_service_15.service;

import ra.java_service_15.model.dto.request.UpdateRoleRequest;
import ra.java_service_15.model.dto.request.UserLogin;
import ra.java_service_15.model.dto.request.UserRegister;
import ra.java_service_15.model.dto.response.JWTResponse;
import ra.java_service_15.model.dto.response.UserInfoResponse;
import ra.java_service_15.model.entity.User;

public interface UserService {
    User register(UserRegister userRegister);
    JWTResponse login(UserLogin userLogin);


    UserInfoResponse getCurrentUserInfo(String username);


    UserInfoResponse updateUserRole(Long userId, UpdateRoleRequest request);


    User findByUsername(String username);
}

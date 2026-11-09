package in.smitapriya.billingsoftware.service;

import in.smitapriya.billingsoftware.io.UserRequest;
import in.smitapriya.billingsoftware.io.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);
    String getUserRole(String email);
    List<UserResponse> readUsers();
    void deleteUser(String id);
}

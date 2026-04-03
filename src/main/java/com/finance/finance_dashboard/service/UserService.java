package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.config.PasswordEncoder;
import com.finance.finance_dashboard.dto.request.UserRequest;
import com.finance.finance_dashboard.dto.response.UserResponse;
import com.finance.finance_dashboard.enums.Role;
import com.finance.finance_dashboard.exception.EmailAlreadyExistException;
import com.finance.finance_dashboard.exception.UserNotFoundException;
import com.finance.finance_dashboard.model.User;
import com.finance.finance_dashboard.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUserById(UUID id){
        if(userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException("User Not Found with Id: " + id);
        }
        User user = userRepository.findById(id).get();
        return userToUserResponse(user);

    }

    public UserResponse userToUserResponse(User user){
        return new UserResponse(user.getId(),
                user.getName(), user.getEmail(), user.getRole().toString(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getDeletedAt());
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(this::userToUserResponse);
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserRequest userRequest){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User Not Found with Id: " + id);
        }
        User user = userRepository.findById(id).get();
        if(!userRequest.name().isEmpty()){
            user.setName(userRequest.name());
        }
        if(!userRequest.email().isEmpty()){
            user.setEmail(userRequest.email());
        }
        if(!userRequest.password().isEmpty()){
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(userRequest.password()));
        }
        if(!userRequest.role().isEmpty()){
            Role userRole = switch (userRequest.role().toUpperCase().trim()){
                case "VIEWER" -> Role.VIEWER;
                case "ANALYST" -> Role.ANALYST;
                case "ADMIN" -> Role.ADMIN;
                default -> Role.VIEWER;
            };
            user.setRole(userRole);
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return userToUserResponse(updatedUser);
    }

    public void deleteUser(UUID id){
        if(userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException("User Not Found with Id: " + id);
        }

        // soft delete - we set deletedAt as current time only and not actually delete the user from database

        User user = userRepository.findById(id).get();
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public UserResponse createUser(UserRequest userRequest){
        Optional<User> optUser = userRepository.findByEmail(userRequest.email());
        User user;
        if(optUser.isPresent()){
            if(optUser.get().getDeletedAt() == null){
                throw new EmailAlreadyExistException("Email Already Exist");
            }
            else{
                user = optUser.get();
                if(!userRequest.name().isEmpty()){
                    user.setName(userRequest.name());
                }
                if(!userRequest.email().isEmpty()){
                    user.setEmail(userRequest.email());
                }
                if(!userRequest.role().isEmpty()){
                    Role userRole = switch (userRequest.role().toUpperCase().trim()){
                        case "VIEWER" -> Role.VIEWER;
                        case "ANALYST" -> Role.ANALYST;
                        case "ADMIN" -> Role.ADMIN;
                        default -> Role.VIEWER;
                    };
                    user.setRole(userRole);
                }

                user.setUpdatedAt(LocalDateTime.now());

            }
        }
        else{
            user = new User();
            user.setEmail(userRequest.email());
            user.setName(userRequest.name());
            user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode((userRequest.password())));
            Role userRole = switch (userRequest.role().toUpperCase().trim()){
                case "VIEWER" -> Role.VIEWER;
                case "ANALYST" -> Role.ANALYST;
                case "ADMIN" -> Role.ADMIN;
                default -> Role.VIEWER;
            };
            user.setRole(userRole);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

        }
        User savedUser = userRepository.save(user);
        return userToUserResponse(savedUser);
    }
}

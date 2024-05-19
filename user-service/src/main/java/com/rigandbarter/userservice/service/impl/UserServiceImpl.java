package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.userservice.repository.relational.IUserRepository;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.repository.file.IProfilePictureRepository;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final IKeycloakService keycloakService;
    private final IUserRepository userRepository;
    private final IProfilePictureRepository profilePictureRepository;

    @Override
    public String registerUser(UserRegisterRequest userRegisterRequest) {
//        return this.userRepository.save(UserMapper.dtoToEntity(userRegisterRequest)).getUserId();
        this.keycloakService.registerUser(userRegisterRequest);
        return "test";
    }
}

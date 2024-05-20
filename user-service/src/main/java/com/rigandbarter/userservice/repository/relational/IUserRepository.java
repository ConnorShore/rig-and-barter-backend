package com.rigandbarter.userservice.repository.relational;

import com.rigandbarter.userservice.model.UserEntity;

public interface IUserRepository {
    /**
     * Saves the user to the database
     * @param userEntity The user to save
     * @return The user if saved, null otherwise
     */
    UserEntity save(UserEntity userEntity);
}

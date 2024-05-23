package com.rigandbarter.userservice.repository.relational;

import com.rigandbarter.userservice.model.UserEntity;

public interface IUserRepository {
    /**
     * Saves the user to the database
     * @param userEntity The user to save
     * @return The user if saved, null otherwise
     */
    UserEntity save(UserEntity userEntity);

    /**
     * Returns user with associated email
     * @param email The email of the user to retrieve
     * @return The UserEntity with associated email, null if not found
     */
    UserEntity findByEmail(String email);

    /**
     * Returns user with associated uid
     * @param userId The uid of the user to retrieve
     * @return The UserEntity with associated email, null if not found
     */
    UserEntity findByUserId(String userId);
}

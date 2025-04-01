package com.rigandbarter.userservice.repository.relational.jpa;

import com.rigandbarter.userservice.model.UserEntity;
import com.rigandbarter.userservice.repository.relational.IUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaUserRepositoryImpl extends IUserRepository, JpaRepository<UserEntity, Long> {
}

package com.popelyshko.alerthub.repository;

import com.popelyshko.alerthub.model.LinkCode;
import com.popelyshko.alerthub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkCodeRepository extends JpaRepository<LinkCode, Long> {
    Optional<LinkCode> findByCode(String code);
    Optional<LinkCode> findByUser(User user);
}

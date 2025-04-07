package org.yvesguilherme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yvesguilherme.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

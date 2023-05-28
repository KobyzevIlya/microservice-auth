package ru.hse.hw4.auth.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.hse.hw4.auth.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findByEmailAndPasswrd(String email, String passwrd);
}

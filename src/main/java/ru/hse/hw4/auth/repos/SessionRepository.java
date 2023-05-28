package ru.hse.hw4.auth.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.hse.hw4.auth.domain.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findByToken(String token);
}
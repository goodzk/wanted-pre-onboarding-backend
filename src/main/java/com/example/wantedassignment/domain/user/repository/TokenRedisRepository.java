package com.example.wantedassignment.domain.user.repository;

import com.example.wantedassignment.domain.user.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}

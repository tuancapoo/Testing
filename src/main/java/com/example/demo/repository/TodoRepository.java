package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Todo;

@Repository
public interface TodoRepository
        extends org.springframework.data.jpa.repository.JpaRepository<com.example.demo.Entities.Todo, Long> {
    Todo save(Todo todo);
}

package com.example.demo.services;

import java.util.List;

import javax.swing.text.html.parser.Entity;

import org.springframework.stereotype.Service;

import com.example.demo.Entities.Todo;
import com.example.demo.repository.TodoRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
    TodoRepository todoRepository;
    @PersistenceContext
    EntityManager entityManager;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public Todo createTodo(String username) {
        Todo todo = new Todo(username);
        entityManager.persist(todo);
        todo = entityManager.find(Todo.class, todo.getId());
        return todo;
    }

    @Transactional
    public List<Todo> getTodos() {
        return entityManager.createQuery("SELECT t FROM Todo t", Todo.class)
                .getResultList();
    }

    @Transactional
    public Todo updateTodo(Long id, Todo todo) {
        Todo existingTodo = entityManager.find(Todo.class, id);
        if (existingTodo != null) {
            existingTodo.setUsername(todo.getUsername());
            existingTodo.setCompleted(todo.isCompleted());
            entityManager.merge(existingTodo);
        }
        return existingTodo;
    }

    @Transactional
    public void deleteTodo(Long id) {
        Todo todo = entityManager.find(Todo.class, id);
        if (todo != null) {
            entityManager.remove(todo);
        }
    }

    @Transactional
    public Todo getTodoById(Long id) {
        return entityManager.find(Todo.class, id);
    }

}

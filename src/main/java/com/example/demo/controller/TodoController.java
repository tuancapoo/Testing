package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.Todo;
import com.example.demo.services.TodoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class TodoController {
    TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok().body("Hello World");
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> todos = todoService.getTodos();
        return ResponseEntity.ok().body(todos);
    }

    @GetMapping("/todos/{todo_id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long todo_id) {
        Todo todo = todoService.getTodoById(todo_id);
        return ResponseEntity.ok().body(todo);
    }

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(
            @RequestBody Todo todo) {
        // TODO: process POST request
        Todo createdTodo = todoService.createTodo(todo.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);

    }

    @PutMapping("/todos/{todo_id}")
    public ResponseEntity<Todo> updateTodo(
            @PathVariable Long todo_id,
            @RequestBody Todo todo) {
        Todo updatedTodo = todoService.updateTodo(todo_id, todo);
        return ResponseEntity.ok().body(updatedTodo);
    }

    @DeleteMapping("/todos/{todo_id}")
    public ResponseEntity<String> deleteTodo(
            @PathVariable Long todo_id) {
        todoService.deleteTodo(todo_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Successfully");
    }
    // ----------------------------------

}

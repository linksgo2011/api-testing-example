package cn.printf.api.controller;

import cn.printf.api.domain.todo.Todo;
import cn.printf.api.domain.todo.TodoRepository;
import cn.printf.api.controller.dto.ResourceWithUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/todos")
public class TodoController {
    private TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping
    public HttpEntity<Collection<ResourceWithUrl>> getAll() {
        List<ResourceWithUrl> resourceWithUrls = todoRepository.getAll().stream()
                .map(this::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resourceWithUrls);
    }

    @GetMapping("/{todo-id}")
    public HttpEntity<ResourceWithUrl> getTodo(@PathVariable("todo-id") long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);

        return todoOptional
                .map(todo -> respondWithResource(todo, OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(headers = {"Content-type=application/json"})
    public HttpEntity<ResourceWithUrl> saveTodo(@RequestBody Todo todo) {
        todo.setId(todoRepository.getAll().size() + 1);
        todoRepository.add(todo);

        return respondWithResource(todo, CREATED);
    }

    @DeleteMapping("/{todo-id}")
    public ResponseEntity deleteOneTodo(@PathVariable("todo-id") long id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);

        if (todoOptional.isPresent()) {
            todoRepository.delete(todoOptional.get());
            return new ResponseEntity<>(OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PatchMapping(value = "/{todo-id}", headers = {"Content-type=application/json"})
    public HttpEntity<ResourceWithUrl> updateTodo(@PathVariable("todo-id") long id, @RequestBody Todo newTodo) {
        Optional<Todo> todoOptional = todoRepository.findById(id);

        if (!todoOptional.isPresent()) {
            return new ResponseEntity<>(NOT_FOUND);
        } else if (newTodo == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        todoRepository.delete(todoOptional.get());

        Todo mergedTodo = todoOptional.get().merge(newTodo);
        todoRepository.add(mergedTodo);

        return respondWithResource(mergedTodo, OK);
    }


    private ResourceWithUrl toResource(Todo todo) {
        return new ResourceWithUrl(todo);
    }

    private HttpEntity<ResourceWithUrl> respondWithResource(Todo todo, HttpStatus statusCode) {
        ResourceWithUrl resourceWithUrl = toResource(todo);

        return new ResponseEntity<>(resourceWithUrl, statusCode);
    }
}

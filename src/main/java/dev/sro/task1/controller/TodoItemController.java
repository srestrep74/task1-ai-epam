package dev.sro.task1.controller;

import dev.sro.task1.entity.TodoItem;
import dev.sro.task1.exception.ResourceNotFoundException;
import dev.sro.task1.service.TodoItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/api/todos") 
public class TodoItemController {

    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    /**
     * Retrieves a list of all todo items.
     * GET /api/todos
     * @return A ResponseEntity containing a list of TodoItem objects and HTTP 200 OK status.
     */
    @GetMapping
    public ResponseEntity<List<TodoItem>> getAllTodoItems() {
        List<TodoItem> todoItems = todoItemService.getAllTodoItems();
        return ResponseEntity.ok(todoItems); // Returns HTTP 200 OK
    }

    /**
     * Retrieves a specific todo item by its ID.
     * GET /api/todos/{id}
     * @param id The ID of the todo item to retrieve.
     * @return A ResponseEntity containing the TodoItem and HTTP 200 OK status if found.
     * @throws ResourceNotFoundException if the todo item with the given ID is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoItemById(@PathVariable Long id) {
        // The service layer's getTodoItemById returns an Optional.
        // If not found, orElseThrow will throw ResourceNotFoundException,
        // which Spring will automatically map to 404 Not Found due to @ResponseStatus on the exception.
        TodoItem todoItem = todoItemService.getTodoItemById(id)
                                          .orElseThrow(() -> new ResourceNotFoundException("TodoItem not found with id: " + id));
        return ResponseEntity.ok(todoItem); // Returns HTTP 200 OK
    }

    /**
     * Creates a new todo item.
     * POST /api/todos
     * @param todoItem The TodoItem object received in the request body.
     * @return A ResponseEntity containing the created TodoItem and HTTP 201 Created status.
     * @throws IllegalArgumentException if the todo item data (e.g., title) is invalid.
     */
    @PostMapping
    public ResponseEntity<TodoItem> createTodoItem(@RequestBody TodoItem todoItem) {
        TodoItem createdTodoItem = todoItemService.createTodoItem(todoItem);
        // Returns HTTP 201 Created and the newly created resource
        return new ResponseEntity<>(createdTodoItem, HttpStatus.CREATED);
    }

    /**
     * Updates an existing todo item.
     * PUT /api/todos/{id}
     * @param id The ID of the todo item to update.
     * @param todoItemDetails The TodoItem object containing the updated details.
     * @return A ResponseEntity containing the updated TodoItem and HTTP 200 OK status.
     * @throws ResourceNotFoundException if the todo item with the given ID is not found.
     * @throws IllegalArgumentException if the updated todo item data (e.g., title) is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long id, @RequestBody TodoItem todoItemDetails) {
        TodoItem updatedTodoItem = todoItemService.updateTodoItem(id, todoItemDetails);
        return ResponseEntity.ok(updatedTodoItem); // Returns HTTP 200 OK
    }

    /**
     * Deletes a todo item by its ID.
     * DELETE /api/todos/{id}
     * @param id The ID of the todo item to delete.
     * @return A ResponseEntity with HTTP 204 No Content status.
     * @throws ResourceNotFoundException if the todo item with the given ID is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build(); // Returns HTTP 204 No Content
    }

    // --- Optional: Custom Exception Handling for IllegalArgumentException ---
    // While ResourceNotFoundException is handled via @ResponseStatus,
    // IllegalArgumentException (e.g., for empty title) might need explicit handling
    // if you want a specific error message or status code (like 400 Bad Request).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // Returns HTTP 400 Bad Request
    }

    // --- Search Endpoints using custom service methods ---

    /**
     * Searches for todo items by title.
     * GET /api/todos/search/title?query=someTitle
     * @param query The title query string.
     * @return A list of TodoItem objects matching the search criteria.
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<TodoItem>> searchTodoItemsByTitle(@RequestParam("query") String query) {
        List<TodoItem> todoItems = todoItemService.searchTodoItemsByTitle(query);
        return ResponseEntity.ok(todoItems);
    }

    /**
     * Searches for todo items by description.
     * GET /api/todos/search/description?query=someDescription
     * @param query The description query string.
     * @return A list of TodoItem objects matching the search criteria.
     */
    @GetMapping("/search/description")
    public ResponseEntity<List<TodoItem>> searchTodoItemsByDescription(@RequestParam("query") String query) {
        List<TodoItem> todoItems = todoItemService.searchTodoItemsByDescription(query);
        return ResponseEntity.ok(todoItems);
    }
}
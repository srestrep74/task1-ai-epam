package dev.sro.task1.service;

import dev.sro.task1.entity.TodoItem;
import java.util.List;
import java.util.Optional;

public interface TodoItemService {

    /**
     * Retrieves all todo items.
     * @return A list of all TodoItem objects.
     */
    List<TodoItem> getAllTodoItems();

    /**
     * Retrieves a single todo item by its ID.
     * @param id The ID of the todo item to retrieve.
     * @return An Optional containing the TodoItem if found, or empty if not.
     */
    Optional<TodoItem> getTodoItemById(Long id);

    /**
     * Creates a new todo item.
     * @param todoItem The TodoItem object to create.
     * @return The created TodoItem object with its generated ID.
     * @throws IllegalArgumentException if the title is null or empty.
     */
    TodoItem createTodoItem(TodoItem todoItem);

    /**
     * Updates an existing todo item.
     * @param id The ID of the todo item to update.
     * @param todoItemDetails The TodoItem object containing the updated details.
     * @return The updated TodoItem object.
     * @throws com.todoapp.exception.ResourceNotFoundException if the todo item with the given ID is not found.
     * @throws IllegalArgumentException if the title in todoItemDetails is null or empty.
     */
    TodoItem updateTodoItem(Long id, TodoItem todoItemDetails);

    /**
     * Deletes a todo item by its ID.
     * @param id The ID of the todo item to delete.
     * @throws com.todoapp.exception.ResourceNotFoundException if the todo item with the given ID is not found.
     */
    void deleteTodoItem(Long id);

    /**
     * Searches for todo items by a partial match in their title (case-insensitive).
     * @param title The partial title to search for.
     * @return A list of TodoItem objects matching the search criteria.
     */
    List<TodoItem> searchTodoItemsByTitle(String title);

    /**
     * Searches for todo items by a partial match in their description (case-insensitive).
     * @param description The partial description to search for.
     * @return A list of TodoItem objects matching the search criteria.
     */
    List<TodoItem> searchTodoItemsByDescription(String description);
}
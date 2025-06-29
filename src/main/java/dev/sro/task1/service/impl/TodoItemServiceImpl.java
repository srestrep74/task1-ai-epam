package dev.sro.task1.service.impl;

import dev.sro.task1.entity.TodoItem;
import dev.sro.task1.exception.ResourceNotFoundException;
import dev.sro.task1.repository.TodoItemRepository;
import dev.sro.task1.service.TodoItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service 
@Transactional 
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository todoItemRepository;

    public TodoItemServiceImpl(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    /**
     * Retrieves all todo items from the database.
     * @return A list of all TodoItem objects.
     */
    @Override
    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    /**
     * Retrieves a single todo item by its ID.
     * @param id The ID of the todo item to retrieve.
     * @return An Optional containing the TodoItem if found, or empty if not.
     */
    @Override
    public Optional<TodoItem> getTodoItemById(Long id) {
        return todoItemRepository.findById(id);
    }

    /**
     * Creates a new todo item.
     * Includes basic validation to ensure the title is not null or empty.
     * @param todoItem The TodoItem object to create.
     * @return The created TodoItem object with its generated ID.
     * @throws IllegalArgumentException if the title is null or empty.
     */
    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {
        // Basic validation: Title must not be null or empty
        if (todoItem.getTitle() == null || todoItem.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Todo item title cannot be null or empty.");
        }
        // Save the new todo item
        return todoItemRepository.save(todoItem);
    }

    /**
     * Updates an existing todo item.
     * Fetches the existing item, updates its fields, and then saves it.
     * Handles cases where the item to be updated does not exist.
     * @param id The ID of the todo item to update.
     * @param todoItemDetails The TodoItem object containing the updated details.
     * @return The updated TodoItem object.
     * @throws ResourceNotFoundException if the todo item with the given ID is not found.
     * @throws IllegalArgumentException if the title in todoItemDetails is null or empty.
     */
    @Override
    public TodoItem updateTodoItem(Long id, TodoItem todoItemDetails) {
        // Basic validation: Title must not be null or empty for updates
        if (todoItemDetails.getTitle() == null || todoItemDetails.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Updated todo item title cannot be null or empty.");
        }

        // Find the existing todo item by ID
        TodoItem existingTodoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TodoItem not found with id: " + id));

        // Update the fields of the existing item
        existingTodoItem.setTitle(todoItemDetails.getTitle());
        // Only update description if it's provided in the details (allowing null for description)
        existingTodoItem.setDescription(todoItemDetails.getDescription());

        // Save the updated todo item
        return todoItemRepository.save(existingTodoItem);
    }

    /**
     * Deletes a todo item by its ID.
     * Checks if the item exists before attempting to delete it.
     * @param id The ID of the todo item to delete.
     * @throws ResourceNotFoundException if the todo item with the given ID is not found.
     */
    @Override
    public void deleteTodoItem(Long id) {
        // Check if the todo item exists before deleting
        if (!todoItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("TodoItem not found with id: " + id);
        }
        todoItemRepository.deleteById(id);
    }

    /**
     * Searches for todo items by a partial match in their title (case-insensitive).
     * Delegates to the custom query method in the repository.
     * @param title The partial title to search for.
     * @return A list of TodoItem objects matching the search criteria.
     */
    @Override
    public List<TodoItem> searchTodoItemsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of(); // Return empty list if search query is empty
        }
        return todoItemRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Searches for todo items by a partial match in their description (case-insensitive).
     * Delegates to the custom query method in the repository.
     * @param description The partial description to search for.
     * @return A list of TodoItem objects matching the search criteria.
     */
    @Override
    public List<TodoItem> searchTodoItemsByDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return List.of(); // Return empty list if search query is empty
        }
        return todoItemRepository.findByDescriptionContainingIgnoreCase(description);
    }
}
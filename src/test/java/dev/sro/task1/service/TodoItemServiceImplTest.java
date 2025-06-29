package dev.sro.task1.service; 

import dev.sro.task1.entity.TodoItem;
import dev.sro.task1.exception.ResourceNotFoundException;
import dev.sro.task1.repository.TodoItemRepository;
import dev.sro.task1.service.impl.TodoItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
@DisplayName("TodoItemServiceImpl Unit Tests")
class TodoItemServiceImplTest {

    @Mock 
    private TodoItemRepository todoItemRepository;

    @InjectMocks 
    private TodoItemServiceImpl todoItemService;

    private TodoItem todo1;
    private TodoItem todo2;

    @BeforeEach
    void setUp() {
        todo1 = new TodoItem("Buy groceries", "Milk, Eggs, Bread");
        todo1.setId(1L); // Set ID for existing item scenario

        todo2 = new TodoItem("Plan vacation", "Research destinations and book flights");
        todo2.setId(2L); // Set ID for existing item scenario
    }

    @Test
    @DisplayName("getAllTodoItems should return all items from the repository")
    void getAllTodoItems_shouldReturnAllItems() {
        // Arrange
        List<TodoItem> expectedTodos = Arrays.asList(todo1, todo2);
        when(todoItemRepository.findAll()).thenReturn(expectedTodos);

        // Act
        List<TodoItem> actualTodos = todoItemService.getAllTodoItems();

        // Assert
        assertNotNull(actualTodos);
        assertEquals(2, actualTodos.size());
        assertEquals(expectedTodos, actualTodos);
        // Verify that findAll was called exactly once on the mock repository
        verify(todoItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getTodoItemById should return the correct item if it exists")
    void getTodoItemById_shouldReturnItemIfExists() {
        // Arrange
        Long itemId = 1L;
        when(todoItemRepository.findById(itemId)).thenReturn(Optional.of(todo1));

        // Act
        Optional<TodoItem> actualTodoOptional = todoItemService.getTodoItemById(itemId);

        // Assert
        assertTrue(actualTodoOptional.isPresent());
        assertEquals(todo1, actualTodoOptional.get());
        verify(todoItemRepository, times(1)).findById(itemId);
    }

    @Test
    @DisplayName("getTodoItemById should return an empty Optional if the item does not exist")
    void getTodoItemById_shouldReturnEmptyOptionalIfItemDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        when(todoItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<TodoItem> actualTodoOptional = todoItemService.getTodoItemById(nonExistentId);

        // Assert
        assertFalse(actualTodoOptional.isPresent());
        verify(todoItemRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("createTodoItem should save and return the new item when the title is valid")
    void createTodoItem_shouldSaveAndReturnNewItem_whenTitleIsValid() {
        // Arrange
        TodoItem newItem = new TodoItem("New Task", "Description for new task");
        // Mock the behavior of save method: it should return the item it receives (simulating successful save with ID)
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(newItem);

        // Act
        TodoItem createdItem = todoItemService.createTodoItem(newItem);

        // Assert
        assertNotNull(createdItem);
        assertEquals("New Task", createdItem.getTitle());
        assertEquals("Description for new task", createdItem.getDescription());
        // Verify that save method was called exactly once with the new item
        verify(todoItemRepository, times(1)).save(newItem);
    }

    @Test
    @DisplayName("createTodoItem should throw IllegalArgumentException if the title is null")
    void createTodoItem_shouldThrowIllegalArgumentException_whenTitleIsNull() {
        // Arrange
        TodoItem invalidItem = new TodoItem(null, "Description");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            todoItemService.createTodoItem(invalidItem);
        });
        assertEquals("Todo item title cannot be null or empty.", thrown.getMessage());
        // Verify that save method was never called
        verify(todoItemRepository, never()).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("createTodoItem should throw IllegalArgumentException if the title is blank")
    void createTodoItem_shouldThrowIllegalArgumentException_whenTitleIsBlank() {
        // Arrange
        TodoItem invalidItem = new TodoItem("   ", "Description");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            todoItemService.createTodoItem(invalidItem);
        });
        assertEquals("Todo item title cannot be null or empty.", thrown.getMessage());
        // Verify that save method was never called
        verify(todoItemRepository, never()).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("updateTodoItem should update and return the modified item if it exists and title is valid")
    void updateTodoItem_shouldUpdateAndReturnModifiedItem_whenItemExistsAndTitleIsValid() {
        // Arrange
        Long itemId = 1L;
        TodoItem updatedDetails = new TodoItem("Updated Groceries", "Milk, Eggs, Bread, Cheese");
        // Mock finding the existing item
        when(todoItemRepository.findById(itemId)).thenReturn(Optional.of(todo1));
        // Mock saving the updated item
        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todo1); // todo1 will be modified in service

        // Act
        TodoItem result = todoItemService.updateTodoItem(itemId, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Updated Groceries", result.getTitle());
        assertEquals("Milk, Eggs, Bread, Cheese", result.getDescription());
        // Verify findById was called
        verify(todoItemRepository, times(1)).findById(itemId);
        // Verify save was called with the modified existing item
        verify(todoItemRepository, times(1)).save(todo1);
    }

    @Test
    @DisplayName("updateTodoItem should throw IllegalArgumentException if the new title is null")
    void updateTodoItem_shouldThrowIllegalArgumentException_whenNewTitleIsNull() {
        // Arrange
        Long itemId = 1L;
        TodoItem updatedDetails = new TodoItem(null, "New Description");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            todoItemService.updateTodoItem(itemId, updatedDetails);
        });
        assertEquals("Updated todo item title cannot be null or empty.", thrown.getMessage());
        // Verify that findById or save methods were never called as validation happens first
        verify(todoItemRepository, never()).findById(anyLong());
        verify(todoItemRepository, never()).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("updateTodoItem should throw IllegalArgumentException if the new title is blank")
    void updateTodoItem_shouldThrowIllegalArgumentException_whenNewTitleIsBlank() {
        // Arrange
        Long itemId = 1L;
        TodoItem updatedDetails = new TodoItem("   ", "New Description");

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            todoItemService.updateTodoItem(itemId, updatedDetails);
        });
        assertEquals("Updated todo item title cannot be null or empty.", thrown.getMessage());
        verify(todoItemRepository, never()).findById(anyLong());
        verify(todoItemRepository, never()).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("updateTodoItem should throw ResourceNotFoundException if the item does not exist")
    void updateTodoItem_shouldThrowResourceNotFoundException_whenItemDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        TodoItem updatedDetails = new TodoItem("Non-existent update", "details");
        when(todoItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            todoItemService.updateTodoItem(nonExistentId, updatedDetails);
        });
        assertEquals("TodoItem not found with id: " + nonExistentId, thrown.getMessage());
        // Verify findById was called, but save was not
        verify(todoItemRepository, times(1)).findById(nonExistentId);
        verify(todoItemRepository, never()).save(any(TodoItem.class));
    }

    @Test
    @DisplayName("deleteTodoItem should delete the item if it exists")
    void deleteTodoItem_shouldDeleteItemIfExists() {
        // Arrange
        Long itemId = 1L;
        // Mock that the item exists
        when(todoItemRepository.existsById(itemId)).thenReturn(true);
        // Do nothing when deleteById is called (default for void methods)
        doNothing().when(todoItemRepository).deleteById(itemId);

        // Act
        assertDoesNotThrow(() -> todoItemService.deleteTodoItem(itemId));

        // Assert
        verify(todoItemRepository, times(1)).existsById(itemId);
        verify(todoItemRepository, times(1)).deleteById(itemId);
    }

    @Test
    @DisplayName("deleteTodoItem should throw ResourceNotFoundException if the item does not exist")
    void deleteTodoItem_shouldThrowResourceNotFoundException_whenItemDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        // Mock that the item does not exist
        when(todoItemRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            todoItemService.deleteTodoItem(nonExistentId);
        });
        assertEquals("TodoItem not found with id: " + nonExistentId, thrown.getMessage());
        // Verify existsById was called, but deleteById was not
        verify(todoItemRepository, times(1)).existsById(nonExistentId);
        verify(todoItemRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("searchTodoItemsByTitle should return matching items for valid non-empty string")
    void searchTodoItemsByTitle_shouldReturnMatchingItems() {
        // Arrange
        String query = "buy";
        when(todoItemRepository.findByTitleContainingIgnoreCase(query))
                .thenReturn(Collections.singletonList(todo1));

        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByTitle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(todo1, result.get(0));
        verify(todoItemRepository, times(1)).findByTitleContainingIgnoreCase(query);
    }

    @Test
    @DisplayName("searchTodoItemsByTitle should return an empty list for null input")
    void searchTodoItemsByTitle_shouldReturnEmptyList_whenQueryIsNull() {
        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByTitle(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Verify that the repository method was NOT called
        verify(todoItemRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchTodoItemsByTitle should return an empty list for blank input")
    void searchTodoItemsByTitle_shouldReturnEmptyList_whenQueryIsBlank() {
        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByTitle("   ");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(todoItemRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchTodoItemsByDescription should return matching items for valid non-empty string")
    void searchTodoItemsByDescription_shouldReturnMatchingItems() {
        // Arrange
        String query = "flights";
        when(todoItemRepository.findByDescriptionContainingIgnoreCase(query))
                .thenReturn(Collections.singletonList(todo2));

        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByDescription(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(todo2, result.get(0));
        verify(todoItemRepository, times(1)).findByDescriptionContainingIgnoreCase(query);
    }

    @Test
    @DisplayName("searchTodoItemsByDescription should return an empty list for null input")
    void searchTodoItemsByDescription_shouldReturnEmptyList_whenQueryIsNull() {
        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByDescription(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(todoItemRepository, never()).findByDescriptionContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("searchTodoItemsByDescription should return an empty list for blank input")
    void searchTodoItemsByDescription_shouldReturnEmptyList_whenQueryIsBlank() {
        // Act
        List<TodoItem> result = todoItemService.searchTodoItemsByDescription("   ");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(todoItemRepository, never()).findByDescriptionContainingIgnoreCase(anyString());
    }
}
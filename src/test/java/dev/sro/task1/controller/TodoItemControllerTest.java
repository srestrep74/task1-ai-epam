package dev.sro.task1.controller;

import com.fasterxml.jackson.databind.ObjectMapper; 
import dev.sro.task1.entity.TodoItem;
import dev.sro.task1.exception.ResourceNotFoundException;
import dev.sro.task1.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoItemController.class) 
@DisplayName("TodoItemController Unit Tests")
class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean 
    private TodoItemService todoItemService;

    @Autowired
    private ObjectMapper objectMapper; 

    private TodoItem todo1;
    private TodoItem todo2;

    @BeforeEach
    void setUp() {
        todo1 = new TodoItem("Buy groceries", "Milk, Eggs");
        todo1.setId(1L);

        todo2 = new TodoItem("Workout", "Go to gym for 1 hour");
        todo2.setId(2L);
    }

    @Test
    @DisplayName("GET /api/todos should return all todo items")
    void getAllTodoItems_shouldReturnAllItems() throws Exception {
        List<TodoItem> allTodos = Arrays.asList(todo1, todo2);
        when(todoItemService.getAllTodoItems()).thenReturn(allTodos);

        mockMvc.perform(get("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Buy groceries")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Workout")));

        verify(todoItemService, times(1)).getAllTodoItems();
    }

    @Test
    @DisplayName("GET /api/todos/{id} should return the correct item if found")
    void getTodoItemById_shouldReturnItem_whenFound() throws Exception {
        Long itemId = 1L;
        when(todoItemService.getTodoItemById(itemId)).thenReturn(Optional.of(todo1));

        mockMvc.perform(get("/api/todos/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.title", is(todo1.getTitle())))
                .andExpect(jsonPath("$.description", is(todo1.getDescription())));

        verify(todoItemService, times(1)).getTodoItemById(itemId);
    }

    @Test
    @DisplayName("GET /api/todos/{id} should return 404 Not Found if item does not exist")
    void getTodoItemById_shouldReturn404_whenNotFound() throws Exception {
        Long nonExistentId = 99L;
        when(todoItemService.getTodoItemById(nonExistentId))
                .thenReturn(Optional.empty()); 

        mockMvc.perform(get("/api/todos/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); 

        verify(todoItemService, times(1)).getTodoItemById(nonExistentId);
    }

    @Test
    @DisplayName("POST /api/todos should create a new todo item and return 201 Created")
    void createTodoItem_shouldCreateItem_andReturn201() throws Exception {
        TodoItem newItem = new TodoItem("Learn Spring Boot", "Finish tutorial");
        TodoItem savedItem = new TodoItem("Learn Spring Boot", "Finish tutorial");
        savedItem.setId(3L); 

        when(todoItemService.createTodoItem(any(TodoItem.class))).thenReturn(savedItem);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem))) 
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id", is(savedItem.getId().intValue())))
                .andExpect(jsonPath("$.title", is(savedItem.getTitle())))
                .andExpect(jsonPath("$.description", is(savedItem.getDescription())));

        verify(todoItemService, times(1)).createTodoItem(any(TodoItem.class));
    }

    @Test
    @DisplayName("POST /api/todos should return 400 Bad Request if title is invalid")
    void createTodoItem_shouldReturn400_whenTitleIsInvalid() throws Exception {
        TodoItem invalidItem = new TodoItem("", "Description"); 
        when(todoItemService.createTodoItem(any(TodoItem.class)))
                .thenThrow(new IllegalArgumentException("Todo item title cannot be null or empty."));

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest()) 
                .andExpect(content().string(containsString("Todo item title cannot be null or empty."))); 

        verify(todoItemService, times(1)).createTodoItem(any(TodoItem.class));
    }


    @Test
    @DisplayName("PUT /api/todos/{id} should update an item and return 200 OK")
    void updateTodoItem_shouldUpdateItem_andReturn200() throws Exception {  
        Long itemId = 1L;
        TodoItem updatedDetails = new TodoItem("Updated Groceries", "Milk, Eggs, Bread");
        TodoItem updatedTodo = new TodoItem("Updated Groceries", "Milk, Eggs, Bread");
        updatedTodo.setId(itemId);

        when(todoItemService.updateTodoItem(eq(itemId), any(TodoItem.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.id", is(itemId.intValue())))
                .andExpect(jsonPath("$.title", is("Updated Groceries")))
                .andExpect(jsonPath("$.description", is("Milk, Eggs, Bread")));

        verify(todoItemService, times(1)).updateTodoItem(eq(itemId), any(TodoItem.class));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} should return 404 Not Found if item to update does not exist")
    void updateTodoItem_shouldReturn404_whenItemDoesNotExist() throws Exception {
        Long nonExistentId = 99L;
        TodoItem updatedDetails = new TodoItem("Non-existent Update", "Details");
        when(todoItemService.updateTodoItem(eq(nonExistentId), any(TodoItem.class)))
                .thenThrow(new ResourceNotFoundException("TodoItem not found with id: " + nonExistentId));

        mockMvc.perform(put("/api/todos/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound()); 

        verify(todoItemService, times(1)).updateTodoItem(eq(nonExistentId), any(TodoItem.class));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} should return 400 Bad Request if updated title is invalid")
    void updateTodoItem_shouldReturn400_whenUpdatedTitleIsInvalid() throws Exception {
        Long itemId = 1L;
        TodoItem invalidDetails = new TodoItem(" ", "New Description"); 
        when(todoItemService.updateTodoItem(eq(itemId), any(TodoItem.class)))
                .thenThrow(new IllegalArgumentException("Updated todo item title cannot be null or empty."));

        mockMvc.perform(put("/api/todos/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDetails)))
                .andExpect(status().isBadRequest()) 
                .andExpect(content().string(containsString("Updated todo item title cannot be null or empty.")));

        verify(todoItemService, times(1)).updateTodoItem(eq(itemId), any(TodoItem.class));
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} should delete an item and return 204 No Content")
    void deleteTodoItem_shouldDeleteItem_andReturn204() throws Exception {
        // Arrange
        Long itemId = 1L;
        // Mock service to do nothing (simulating successful deletion)
        doNothing().when(todoItemService).deleteTodoItem(itemId);

        // Act & Assert
        mockMvc.perform(delete("/api/todos/{id}", itemId))
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

        // Verify service method was called
        verify(todoItemService, times(1)).deleteTodoItem(itemId);
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} should return 404 Not Found if item to delete does not exist")
    void deleteTodoItem_shouldReturn404_whenItemDoesNotExist() throws Exception {
        // Arrange
        Long nonExistentId = 99L;
        // Mock service to throw ResourceNotFoundException
        doThrow(new ResourceNotFoundException("TodoItem not found with id: " + nonExistentId))
                .when(todoItemService).deleteTodoItem(nonExistentId);

        // Act & Assert
        mockMvc.perform(delete("/api/todos/{id}", nonExistentId))
                .andExpect(status().isNotFound()); // Expect HTTP 404 Not Found

        // Verify service method was called
        verify(todoItemService, times(1)).deleteTodoItem(nonExistentId);
    }

    @Test
    @DisplayName("GET /api/todos/search/title?query=... should return matching items by title")
    void searchTodoItemsByTitle_shouldReturnMatchingItems() throws Exception {
        // Arrange
        String query = "buy";
        List<TodoItem> matchingTodos = Collections.singletonList(todo1);
        when(todoItemService.searchTodoItemsByTitle(query)).thenReturn(matchingTodos);

        // Act & Assert
        mockMvc.perform(get("/api/todos/search/title")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(todo1.getTitle())));

        // Verify service method was called
        verify(todoItemService, times(1)).searchTodoItemsByTitle(query);
    }

    @Test
    @DisplayName("GET /api/todos/search/title?query= should return empty list for blank query")
    void searchTodoItemsByTitle_shouldReturnEmptyList_whenQueryIsBlank() throws Exception {
        // Arrange
        String query = "   ";
        when(todoItemService.searchTodoItemsByTitle(query)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/todos/search/title")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Verify service method was called
        verify(todoItemService, times(1)).searchTodoItemsByTitle(query);
    }

    @Test
    @DisplayName("GET /api/todos/search/description?query=... should return matching items by description")
    void searchTodoItemsByDescription_shouldReturnMatchingItems() throws Exception {
        // Arrange
        String query = "gym";
        List<TodoItem> matchingTodos = Collections.singletonList(todo2);
        when(todoItemService.searchTodoItemsByDescription(query)).thenReturn(matchingTodos);

        // Act & Assert
        mockMvc.perform(get("/api/todos/search/description")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(todo2.getDescription())));

        // Verify service method was called
        verify(todoItemService, times(1)).searchTodoItemsByDescription(query);
    }

    @Test
    @DisplayName("GET /api/todos/search/description?query= should return empty list for blank query")
    void searchTodoItemsByDescription_shouldReturnEmptyList_whenQueryIsBlank() throws Exception {
        // Arrange
        String query = "";
        when(todoItemService.searchTodoItemsByDescription(query)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/todos/search/description")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Verify service method was called
        verify(todoItemService, times(1)).searchTodoItemsByDescription(query);
    }
}
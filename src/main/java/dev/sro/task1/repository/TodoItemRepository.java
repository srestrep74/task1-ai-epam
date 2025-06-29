package dev.sro.task1.repository; 

import dev.sro.task1.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    /**
     * Custom query method to find todo items by title, ignoring case.
     * Spring Data JPA automatically derives the query from the method name.
     *
     * @param title The title to search for.
     * @return A list of TodoItem objects matching the given title.
     */
    List<TodoItem> findByTitleContainingIgnoreCase(String title);

    /**
     * Custom query method to find todo items by description, ignoring case.
     *
     * @param description The description to search for.
     * @return A list of TodoItem objects matching the given description.
     */
    List<TodoItem> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Custom query method to find todo items where the title contains a given string
     * and sort them by title in ascending order.
     *
     * @param title The partial title to search for.
     * @return A list of TodoItem objects.
     */
    List<TodoItem> findByTitleContainingOrderByTitleAsc(String title);
}
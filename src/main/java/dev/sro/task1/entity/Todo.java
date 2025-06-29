package dev.sro.task1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a Todo item in the todo list management system.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
@Table(name = "todos",
       indexes = {
           @Index(name = "idx_todo_completed", columnList = "completed"),
           @Index(name = "idx_todo_created_at", columnList = "created_at")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_todo_title_created_at", columnNames = {"title", "created_at"})
       }
)
public class Todo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NonNull
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "title", nullable = false, length = 100, columnDefinition = "varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500, columnDefinition = "varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String description;

    @NonNull
    @Column(name = "completed", nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private Boolean completed = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "datetime(6)")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 
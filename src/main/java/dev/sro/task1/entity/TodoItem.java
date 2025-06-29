package dev.sro.task1.entity; 

import jakarta.persistence.*; 

import java.io.Serializable; 

@Entity
@Table(name = "todo_items") 
public class TodoItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 

    @Column(nullable = false, length = 255) 
    private String title;

    @Column(columnDefinition = "TEXT") 
    private String description;

    public TodoItem() {
    }
    
    public TodoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
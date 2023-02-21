package task.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.domain.data.Task;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPersistence {
    private String id;
    private String id_author;
    private String title;
    private String description;
    private String creation_date;
    private List<String> linked_tasks;
}

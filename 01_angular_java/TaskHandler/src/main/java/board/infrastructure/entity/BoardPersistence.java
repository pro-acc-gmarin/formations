package board.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.domain.data.Task;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardPersistence {
    private String id;
    private String id_owner;
    private String title;
    private String description;
    private List<String> linked_tasks;
}

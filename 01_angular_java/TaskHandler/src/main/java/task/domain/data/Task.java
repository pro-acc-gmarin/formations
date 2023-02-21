package task.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    private String id;
    private String id_author;
    private String title;
    private String description;
    private String creation_date;
    private List<String> linked_tasks;
}

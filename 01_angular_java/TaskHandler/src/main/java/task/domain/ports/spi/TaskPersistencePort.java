package task.domain.ports.spi;

import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskPersistencePort {

    Task add(Task task) throws SQLException;

    void delete(String id);

    Task update(Task task, String id) throws SQLException;

    List<Task> getAll() throws SQLException;

    Task getById(String id);
}

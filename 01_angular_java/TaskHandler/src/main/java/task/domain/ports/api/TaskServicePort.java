package task.domain.ports.api;

import task.domain.data.Task;
import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface TaskServicePort {

    Task add(Task task) throws SQLException;

    void delete(String id);

    Task update(Task task, String id) throws SQLException;

    List<Task> getAll() throws SQLException;

    Task getById(String id);

}

package task.domain.ports.api;

import task.domain.data.Task;
import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface TaskServicePort {

    Task add(Task task) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Task update(Task task, String id) throws SQLException, NoSuchMethodException;

    List<Task> getAll() throws SQLException, NoSuchMethodException;

    Task getById(String id) throws SQLException, NoSuchMethodException;

}

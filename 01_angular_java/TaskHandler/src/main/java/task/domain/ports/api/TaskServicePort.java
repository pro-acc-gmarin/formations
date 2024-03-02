package task.domain.ports.api;

import task.domain.data.Task;
import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface TaskServicePort {

    Task add(final Task task) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Task update(final Task task, final String id) throws SQLException, NoSuchMethodException;

    List<Task> getAll() throws SQLException, NoSuchMethodException;

    Task getById(final String id) throws SQLException, NoSuchMethodException;

}

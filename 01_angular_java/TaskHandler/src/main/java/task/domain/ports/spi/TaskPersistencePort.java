package task.domain.ports.spi;

import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskPersistencePort {

    Task add(Task task) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Task update(Task task, String id) throws SQLException, NoSuchMethodException;

    List<Task> getAll() throws SQLException, NoSuchMethodException;

    Task getById(String id) throws SQLException, NoSuchMethodException;
}

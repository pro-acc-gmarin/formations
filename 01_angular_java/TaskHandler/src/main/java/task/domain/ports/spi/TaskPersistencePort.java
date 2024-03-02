package task.domain.ports.spi;

import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskPersistencePort {

    Task add(final Task task) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Task update(final Task task, final String id) throws SQLException, NoSuchMethodException;

    List<Task> getAll() throws SQLException, NoSuchMethodException;

    Task getById(final String id) throws SQLException, NoSuchMethodException;
}

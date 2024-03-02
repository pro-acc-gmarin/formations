package task.infrastructure.spi;

import task.infrastructure.entity.TaskPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TaskDaoSpi {
    TaskPersistence add(final TaskPersistence object) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Optional<TaskPersistence> update(final TaskPersistence object, final String id) throws SQLException, NoSuchMethodException;

    List<TaskPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<TaskPersistence> getById(final String id) throws SQLException, NoSuchMethodException;
}

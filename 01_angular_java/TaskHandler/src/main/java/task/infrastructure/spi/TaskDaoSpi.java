package task.infrastructure.spi;

import task.infrastructure.entity.TaskPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TaskDaoSpi {
    TaskPersistence add(TaskPersistence object) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Optional<TaskPersistence> update(TaskPersistence object, String id) throws SQLException, NoSuchMethodException;

    List<TaskPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<TaskPersistence> getById(String id) throws SQLException, NoSuchMethodException;
}

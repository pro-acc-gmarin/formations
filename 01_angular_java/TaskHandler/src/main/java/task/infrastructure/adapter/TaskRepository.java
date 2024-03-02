package task.infrastructure.adapter;

import task.domain.data.Task;
import task.domain.ports.spi.TaskPersistencePort;
import task.infrastructure.entity.TaskPersistence;
import task.infrastructure.mapper.TaskMapper;
import task.infrastructure.spi.TaskDaoSpi;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements TaskPersistencePort {

    private final TaskDaoSpi repository;
    private final TaskMapper mapper;

    public TaskRepository(final TaskDaoSpi taskDao) {
        this.repository = taskDao;
        this.mapper = TaskMapper.INSTANCE;
    }

    @Override
    public Task add(final Task task) throws SQLException, NoSuchMethodException {
        final TaskPersistence taskPersistence = this.repository.add(mapper.domainToPersistence(task));
        return mapper.persistenceToDomain(taskPersistence);
    }

    @Override
    public void delete(final String id) throws SQLException, NoSuchMethodException {
        this.repository.delete(id);
    }

    @Override
    public Task update(final Task task, final String id) throws SQLException, NoSuchMethodException {
        final Optional<TaskPersistence> oTaskPersistence = this.repository.update(mapper.domainToPersistence(task), id);
        return oTaskPersistence.map(mapper::persistenceToDomain).orElse(null);
    }

    @Override
    public List<Task> getAll() throws SQLException, NoSuchMethodException {
        final List<TaskPersistence> taskPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(taskPersistenceList);
    }

    @Override
    public Task getById(final String id) throws SQLException, NoSuchMethodException {
        final Optional<TaskPersistence> optionalTask = this.repository.getById(id);
        return optionalTask.map(mapper::persistenceToDomain).orElse(null);
    }
}

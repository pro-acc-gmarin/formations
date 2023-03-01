package task.infrastructure.adapter;

import task.domain.data.Task;
import task.domain.ports.spi.TaskPersistencePort;
import task.infrastructure.dao.TaskDao;
import task.infrastructure.entity.TaskPersistence;
import task.infrastructure.mapper.TaskMapper;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements TaskPersistencePort {

    TaskDao repository;
    private final TaskMapper mapper;

    public TaskRepository() throws NamingException {
        this.repository = new task.infrastructure.dao.TaskDao();
        this.mapper = TaskMapper.INSTANCE;
    }

    @Override
    public Task add(Task task) throws SQLException, NoSuchMethodException {
        TaskPersistence taskPersistence = this.repository.add(mapper.domainToPersistence(task));
        return mapper.persistenceToDomain(taskPersistence);
    }

    @Override
    public void delete(String id) throws SQLException, NoSuchMethodException {
        this.repository.delete(id);
    }

    @Override
    public Task update(Task task, String id) throws SQLException, NoSuchMethodException {
        Optional<TaskPersistence> oTaskPersistence= this.repository.update(mapper.domainToPersistence(task), id);
        return oTaskPersistence.map(taskPersistence -> mapper.persistenceToDomain(taskPersistence)).orElse(null);
    }

    @Override
    public List<Task> getAll() throws SQLException, NoSuchMethodException {
        List<TaskPersistence> taskPersistenceList = this.repository.getAll();
        return mapper.persistenceListToDomainList(taskPersistenceList);
    }

    @Override
    public Task getById(String id) throws SQLException, NoSuchMethodException {
        Optional<TaskPersistence> optionalTask = this.repository.getById(id);
        return optionalTask.map(taskPersistence -> mapper.persistenceToDomain(taskPersistence)).orElse(null);
    }
}

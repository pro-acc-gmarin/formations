package task.domain.ports.api;

import task.domain.data.Task;
import task.domain.ports.spi.TaskPersistencePort;
import user.domain.data.User;
import user.domain.ports.api.UserServicePort;
import user.domain.ports.spi.UserPersistencePort;

import java.sql.SQLException;
import java.util.List;

public class TaskServiceImpl implements TaskServicePort {

    private final TaskPersistencePort taskPersistencePort;

    public TaskServiceImpl(final TaskPersistencePort taskPersistencePort) {
        this.taskPersistencePort = taskPersistencePort;
    }

    public Task add(final Task task) throws SQLException, NoSuchMethodException {
        return this.taskPersistencePort.add(task);
    }

    public void delete(final String id) throws SQLException, NoSuchMethodException {
        this.taskPersistencePort.delete(id);
    }

    public Task update(final Task user, final String id) throws SQLException, NoSuchMethodException {
        return this.taskPersistencePort.update(user, id);
    }

    public List<Task> getAll() throws SQLException, NoSuchMethodException {
        return this.taskPersistencePort.getAll();
    }

    public Task getById(final String id) throws SQLException, NoSuchMethodException {
        return this.taskPersistencePort.getById(id);
    }
}

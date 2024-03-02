package task.infrastructure.dao;

import gateway.configuration.transaction.TransactionManager;
import org.apache.commons.lang3.StringUtils;
import task.infrastructure.entity.TaskPersistence;
import task.infrastructure.spi.TaskDaoSpi;
import utils.annotations.MeasurePerformance;
import utils.annotations.PreparedQuery;
import utils.annotations.Transactional;
import utils.helpers.ListHelper;
import utils.helpers.PreparedQueryHelper;

import java.sql.*;
import java.time.Instant;
import java.util.*;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class TaskDao implements TaskDaoSpi {

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("INSERT INTO task (id, id_author, title, description, creation_date, linked_tasks) VALUES (?, ?, ?, ?, ?, ?)")
    public TaskPersistence add(final TaskPersistence taskPersistence) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), TaskPersistence.class), Statement.RETURN_GENERATED_KEYS);
        taskPersistence.setId(this.generateUUID());
        taskPersistence.setId_author("current");
        taskPersistence.setCreation_date(Instant.now().toString());
        statement.setString(1, taskPersistence.getId());
        statement.setString(2, taskPersistence.getId_author());
        statement.setString(3, taskPersistence.getTitle());
        statement.setString(4, taskPersistence.getDescription());
        statement.setString(5, taskPersistence.getCreation_date());

        final Optional<List<String>> linkedTasks = ofNullable(taskPersistence.getLinked_tasks());
        if (linkedTasks.isPresent()) {
            statement.setString(6, ListHelper.joinListString(taskPersistence.getLinked_tasks()));
        } else {
            taskPersistence.setLinked_tasks(Collections.EMPTY_LIST);
            statement.setString(6, StringUtils.EMPTY);
        }

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating task failed, no rows affected.");
        }

        return taskPersistence;
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("DELETE FROM task WHERE id = ?")
    public void delete(final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class), Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting task failed, no rows affected.");
        }

    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("UPDATE task SET title = COALESCE(?, title), description = COALESCE(?, description), linked_tasks = COALESCE(? , linked_tasks) WHERE id = ?")
    public Optional<TaskPersistence> update(final TaskPersistence taskPersistence, final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Optional<TaskPersistence> oTaskPersistence = this.getById(id);
        if (oTaskPersistence.isPresent()) {
            final Connection connection = TransactionManager.getConnection();
            final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), TaskPersistence.class, String.class), Statement.RETURN_GENERATED_KEYS);
            final String title = taskPersistence.getTitle();
            final String description = taskPersistence.getDescription();
            final List<String> updatedListsLinked = new ArrayList<>();
            statement.setString(1, title);
            statement.setString(2, description);
            final Optional<List<String>> oLinkedTasks = ofNullable(taskPersistence.getLinked_tasks());
            if (oLinkedTasks.isPresent()) {
                updatedListsLinked.addAll(oLinkedTasks.get());
                statement.setString(3, ListHelper.joinListString(oLinkedTasks.get()));
            } else {
                statement.setNull(3, java.sql.Types.VARCHAR);
            }
            statement.setString(4, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating task failed, no rows affected.");
            }
            return this.getById(id);
        } else {
            return ofNullable(this.add(taskPersistence));
        }
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("SELECT * FROM task")
    public List<TaskPersistence> getAll() throws SQLException, NoSuchMethodException {
        final List<TaskPersistence> taskPersistenceList = new ArrayList<>();
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithoutParameter(currentMethodName, this.getClass()));

        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            final TaskPersistence taskPersistence = new TaskPersistence();
            taskPersistence.setId(resultSet.getString("id"));
            taskPersistence.setId_author(resultSet.getString("id_author"));
            taskPersistence.setTitle(resultSet.getString("title"));
            taskPersistence.setDescription(resultSet.getString("description"));
            taskPersistence.setCreation_date(resultSet.getString("creation_date"));
            taskPersistence.setLinked_tasks(ListHelper.splitStringToList(resultSet.getString("linked_tasks")));
            taskPersistenceList.add(taskPersistence);
        }

        return taskPersistenceList;
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("SELECT * FROM task WHERE id = ?")
    public Optional<TaskPersistence> getById(final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class));
        statement.setString(1, id);

        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            TaskPersistence taskPersistence = new TaskPersistence();
            taskPersistence.setId(resultSet.getString("id"));
            taskPersistence.setId_author(resultSet.getString("id_author"));
            taskPersistence.setTitle(resultSet.getString("title"));
            taskPersistence.setDescription(resultSet.getString("description"));
            taskPersistence.setCreation_date(resultSet.getString("creation_date"));
            taskPersistence.setLinked_tasks(ListHelper.splitStringToList(resultSet.getString("linked_tasks")));
            return of(taskPersistence);
        }

        return Optional.empty();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String getMethodName(final String methodName) {
        final String[] methodNameParts = methodName.split("_");
        return methodNameParts[0];
    }
}

package task.infrastructure.dao;

import org.apache.commons.lang3.StringUtils;
import task.infrastructure.dao.spi.IDao;
import task.infrastructure.entity.TaskPersistence;
import utils.annotations.PreparedQuery;
import utils.helpers.ListHelper;
import utils.helpers.PreparedQueryHelper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.*;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class TaskDao implements IDao<TaskPersistence, String> {

    DataSource dataSource;

    public TaskDao() throws NamingException {
        Context ctx = new InitialContext();
        Context initCtx = (Context) ctx.lookup("java:/comp/env");
        this.dataSource = (DataSource) initCtx.lookup("jdbc/AppTHDB");
    }

    @Override
    @PreparedQuery("INSERT INTO task (id, id_author, title, description, creation_date, linked_tasks) VALUES (?, ?, ?, ?, ?, ?)")
    public TaskPersistence add(TaskPersistence taskPersistence) throws SQLException {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), TaskPersistence.class), Statement.RETURN_GENERATED_KEYS);
            taskPersistence.setId(this.generateUUID());
            taskPersistence.setId_author("current");
            taskPersistence.setCreation_date(Instant.now().toString());
            statement.setString(1, taskPersistence.getId());
            statement.setString(2, taskPersistence.getId_author());
            statement.setString(3, taskPersistence.getTitle());
            statement.setString(4, taskPersistence.getDescription());
            statement.setString(5, taskPersistence.getCreation_date());

            Optional<List<String>> linkedTasks = ofNullable(taskPersistence.getLinked_tasks());
            if(linkedTasks.isPresent()){
                statement.setString(6, ListHelper.joinListString(taskPersistence.getLinked_tasks()));
            }else{
                taskPersistence.setLinked_tasks(Collections.EMPTY_LIST);
                statement.setString(6, StringUtils.EMPTY);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return taskPersistence;
    }

    @Override
    @PreparedQuery("DELETE FROM task WHERE id = ?")
    public void delete(String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        } catch (NoSuchMethodException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreparedQuery("UPDATE task SET title = ?, description = ?, linked_tasks = COALESCE(? , linked_tasks) WHERE id = ?")
    public Optional<TaskPersistence> update(TaskPersistence taskPersistence, String id) throws SQLException {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Optional<TaskPersistence> oTaskPersistence = this.getById(id);
        if (oTaskPersistence.isPresent()) {
            try (Connection connection = this.dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), TaskPersistence.class, String.class), Statement.RETURN_GENERATED_KEYS);
                String title = taskPersistence.getTitle();
                String description = taskPersistence.getDescription();
                List<String> updatedListsLinked = new ArrayList<>();
                statement.setString(1, title);
                statement.setString(2, description);

                Optional<List<String>> oLinkedTasks = ofNullable(taskPersistence.getLinked_tasks());
                if (oLinkedTasks.isPresent()) {
                    updatedListsLinked.addAll(oLinkedTasks.get());
                    statement.setString(3, ListHelper.joinListString(oLinkedTasks.get()));
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }

                statement.setString(4, id);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating user failed, no rows affected.");
                }

                TaskPersistence updatedTaskPersistence = oTaskPersistence.get();
                updatedTaskPersistence.setTitle(title);
                updatedTaskPersistence.setDescription(description);
                updatedTaskPersistence.setLinked_tasks(updatedListsLinked);
                return of(updatedTaskPersistence);
            } catch (NoSuchMethodException | SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ofNullable(this.add(taskPersistence));
        }
    }

    @Override
    @PreparedQuery("SELECT * FROM task")
    public List<TaskPersistence> getAll() throws SQLException {
        List<TaskPersistence> taskPersistenceList = new ArrayList<>();
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithoutParameter(currentMethodName, this.getClass()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TaskPersistence taskPersistence = new TaskPersistence();
                taskPersistence.setId(resultSet.getString("id"));
                taskPersistence.setId_author(resultSet.getString("id_author"));
                taskPersistence.setTitle(resultSet.getString("title"));
                taskPersistence.setDescription(resultSet.getString("description"));
                taskPersistence.setCreation_date(resultSet.getString("creation_date"));
                taskPersistence.setLinked_tasks(ListHelper.splitStringToList(resultSet.getString("linked_tasks")));

                taskPersistenceList.add(taskPersistence);
            }
            return taskPersistenceList;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreparedQuery("SELECT * FROM task WHERE id = ?")
    public Optional<TaskPersistence> getById(String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class));
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
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
        } catch (NoSuchMethodException | SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

package board.infrastructure.dao;

import board.infrastructure.entity.BoardPersistence;
import org.apache.commons.lang3.StringUtils;
import task.infrastructure.entity.TaskPersistence;
import user.infrastructure.dao.spi.IDao;
import utils.annotations.PreparedQuery;
import utils.helpers.ListHelper;
import utils.helpers.PreparedQueryHelper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class BoardDao implements IDao<BoardPersistence, String> {

    DataSource dataSource;

    public BoardDao() throws NamingException {
        Context ctx = new InitialContext();
        Context initCtx = (Context) ctx.lookup("java:/comp/env");
        this.dataSource = (DataSource) initCtx.lookup("jdbc/AppTHDB");
    }

    @Override
    @PreparedQuery("INSERT INTO board (id, id_owner, title, description, linked_tasks) VALUES (?, ?, ?, ?, ?)")
    public BoardPersistence add(BoardPersistence boardPersistence) throws SQLException {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), BoardPersistence.class), Statement.RETURN_GENERATED_KEYS);
            boardPersistence.setId(this.generateUUID());
            boardPersistence.setId_owner("current");
            statement.setString(1, boardPersistence.getId());
            statement.setString(2, boardPersistence.getId_owner());
            statement.setString(3, boardPersistence.getTitle());
            statement.setString(4, boardPersistence.getDescription());

            Optional<List<String>> linkedTasks = ofNullable(boardPersistence.getLinked_tasks());
            if (linkedTasks.isPresent()) {
                statement.setString(5, ListHelper.joinListString(boardPersistence.getLinked_tasks()));
            } else {
                boardPersistence.setLinked_tasks(Collections.EMPTY_LIST);
                statement.setString(5, StringUtils.EMPTY);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return boardPersistence;
    }

    @Override
    @PreparedQuery("DELETE FROM board WHERE id = ?")
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
    @PreparedQuery("UPDATE board SET title = ?, description = ?, linked_tasks = COALESCE(? , linked_tasks) WHERE id = ?")
    public Optional<BoardPersistence> update(BoardPersistence boardPersistence, String id) throws SQLException {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        Optional<BoardPersistence> oBoardPersistence = this.getById(id);
        if (oBoardPersistence.isPresent()) {
            try (Connection connection = this.dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), BoardPersistence.class, String.class), Statement.RETURN_GENERATED_KEYS);
                String title = boardPersistence.getTitle();
                String description = boardPersistence.getDescription();
                List<String> updatedListsLinked = new ArrayList<>();
                statement.setString(1, title);
                statement.setString(2, description);
                statement.setString(3, id);

                Optional<List<String>> oLinkedTasks = ofNullable(boardPersistence.getLinked_tasks());
                if (oLinkedTasks.isPresent()) {
                    updatedListsLinked.addAll(oLinkedTasks.get());
                    statement.setString(3, ListHelper.joinListString(oLinkedTasks.get()));
                } else {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                }

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating user failed, no rows affected.");
                }

                BoardPersistence updatedBoardPersistence = oBoardPersistence.get();
                updatedBoardPersistence.setTitle(title);
                updatedBoardPersistence.setDescription(description);
                updatedBoardPersistence.setLinked_tasks(updatedListsLinked);
                return of(updatedBoardPersistence);
            } catch (NoSuchMethodException | SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ofNullable(this.add(boardPersistence));
        }
    }

    @Override
    @PreparedQuery("SELECT * FROM board")
    public List<BoardPersistence> getAll() throws SQLException {
        List<BoardPersistence> boardPersistenceList = new ArrayList<>();
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithoutParameter(currentMethodName, this.getClass()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BoardPersistence boardPersistence = new BoardPersistence();
                boardPersistence.setId(resultSet.getString("id"));
                boardPersistence.setId_owner(resultSet.getString("id_owner"));
                boardPersistence.setTitle(resultSet.getString("title"));
                boardPersistence.setDescription(resultSet.getString("description"));
                boardPersistence.setLinked_tasks(ListHelper.splitStringToList(resultSet.getString("linked_tasks")));
                boardPersistenceList.add(boardPersistence);
            }
            return boardPersistenceList;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreparedQuery("SELECT * FROM board WHERE id = ?")
    public Optional<BoardPersistence> getById(String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class));
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BoardPersistence boardPersistence = new BoardPersistence();
                boardPersistence.setId(resultSet.getString("id"));
                boardPersistence.setId_owner(resultSet.getString("id_owner"));
                boardPersistence.setTitle(resultSet.getString("title"));
                boardPersistence.setDescription(resultSet.getString("description"));
                boardPersistence.setLinked_tasks(ListHelper.splitStringToList(resultSet.getString("linked_tasks")));
                return of(boardPersistence);
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

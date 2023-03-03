package user.infrastructure.dao;

import gateway.configuration.ConnectionPool;
import user.infrastructure.dao.spi.IDao;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.helper.RoleEnum;
import utils.annotations.PreparedQuery;
import utils.helpers.PreparedQueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;

public class UserDao implements IDao<UserPersistence, String> {

    @Override
    @PreparedQuery("INSERT INTO user (id, firstname, lastname, email, password, role, salt) VALUES (?, ?, ?, ?, ?, ?, ?)")
    public UserPersistence add(UserPersistence userPersistence) throws SQLException {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try(Connection connection = ConnectionPool.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), UserPersistence.class),  Statement.RETURN_GENERATED_KEYS);
            userPersistence.setId(this.generateUUID());
            userPersistence.setRole(RoleEnum.STANDARD.name());
            statement.setString(1, userPersistence.getId());
            statement.setString(2, userPersistence.getFirstname());
            statement.setString(3, userPersistence.getLastname());
            statement.setString(4, userPersistence.getEmail());
            statement.setString(6, userPersistence.getRole());
            PreparedQueryHelper.setPasswordAndSaltWithIndex(statement, 5, 7);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return userPersistence;
    }



    @Override
    @PreparedQuery("DELETE FROM user WHERE id = ?")
    public void delete(String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try(Connection connection = ConnectionPool.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class),  Statement.RETURN_GENERATED_KEYS);
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
    @PreparedQuery("UPDATE user SET firstname = ?, lastname = ?, email = ? WHERE id = ?")
    public Optional<UserPersistence> update(UserPersistence userPersistence, String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try(Connection connection = ConnectionPool.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), UserPersistence.class, String.class),  Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userPersistence.getFirstname());
            statement.setString(2, userPersistence.getLastname());
            statement.setString(3, userPersistence.getEmail());
            statement.setString(4, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (NoSuchMethodException | SQLException e) {
            throw new RuntimeException(e);
        }
        return this.getById(id);
    }

    @Override
    @PreparedQuery("SELECT id, firstname, lastname, email, role FROM user")
    public List<UserPersistence> getAll() throws SQLException {
        List<UserPersistence> userPersistenceList = new ArrayList<>();
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try(Connection connection = ConnectionPool.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithoutParameter(currentMethodName, this.getClass()));
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                UserPersistence userPersistence = new UserPersistence();
                userPersistence.setId(resultSet.getString("id"));
                userPersistence.setFirstname(resultSet.getString("firstname"));
                userPersistence.setLastname(resultSet.getString("lastname"));
                userPersistence.setEmail(resultSet.getString("email"));
                userPersistence.setRole(resultSet.getString("role"));
                userPersistenceList.add(userPersistence);
            }
            return userPersistenceList;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreparedQuery("SELECT * FROM user WHERE id = ?")
    public Optional<UserPersistence> getById(String id) {
        String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        try(Connection connection = ConnectionPool.getInstance().getConnection()){
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class));
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                UserPersistence userPersistence = new UserPersistence();
                userPersistence.setId(resultSet.getString("id"));
                userPersistence.setFirstname(resultSet.getString("firstname"));
                userPersistence.setLastname(resultSet.getString("lastname"));
                userPersistence.setEmail(resultSet.getString("email"));
                userPersistence.setRole(resultSet.getString("role"));
                return of(userPersistence);
            }
        } catch (NoSuchMethodException | SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private String generateUUID(){
        return UUID.randomUUID().toString();
    }
}

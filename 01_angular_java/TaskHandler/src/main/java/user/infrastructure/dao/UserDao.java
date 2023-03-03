package user.infrastructure.dao;

import gateway.configuration.ConnectionPool;
import user.infrastructure.dao.spi.IDao;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.helper.RoleEnum;
import utils.annotations.MeasurePerformance;
import utils.annotations.PreparedQuery;
import utils.helpers.PreparedQueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class UserDao implements IDao<UserPersistence, String> {

    @Override
    @MeasurePerformance
    @PreparedQuery("INSERT INTO user (id, firstname, lastname, email, password, role, salt) VALUES (?, ?, ?, ?, ?, ?, ?)")
    public UserPersistence add(UserPersistence userPersistence) throws SQLException, NoSuchMethodException {

        String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
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
        }

        return userPersistence;
    }

    @Override
    @MeasurePerformance
    @PreparedQuery("DELETE FROM user WHERE id = ?")
    public void delete(String id) throws SQLException, NoSuchMethodException {
        String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class),  Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        }
    }

    @Override
    @MeasurePerformance
    @PreparedQuery("UPDATE user SET firstname = COALESCE(?, firstname), lastname = COALESCE(?, lastname), email = COALESCE(?, email) WHERE id = ?")
    public Optional<UserPersistence> update(UserPersistence userPersistence, String id) throws SQLException, NoSuchMethodException {
        String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        Optional<UserPersistence> oUserPersistence = this.getById(id);
        if(oUserPersistence.isPresent()) {
            try(Connection connection = ConnectionPool.getInstance().getConnection()){
                PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), UserPersistence.class, String.class), Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, userPersistence.getFirstname());
                statement.setString(2, userPersistence.getLastname());
                statement.setString(3, userPersistence.getEmail());
                statement.setString(4, id);
                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Updating user failed, no rows affected.");
                }
            }
            return this.getById(id);
        }else{
            return ofNullable(this.add(userPersistence));
        }
    }

    @Override
    @MeasurePerformance
    @PreparedQuery("SELECT id, firstname, lastname, email, role FROM user")
    public List<UserPersistence> getAll() throws SQLException, NoSuchMethodException {
        List<UserPersistence> userPersistenceList = new ArrayList<>();
        String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
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
        }

        return userPersistenceList;
    }

    @Override
    @MeasurePerformance
    @PreparedQuery("SELECT * FROM user WHERE id = ?")
    public Optional<UserPersistence> getById(String id) throws SQLException, NoSuchMethodException {
        String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
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
        }

        return Optional.empty();
    }

    private String generateUUID(){
        return UUID.randomUUID().toString();
    }

    private String getMethodName(String methodName){
        String[] methodNameParts = methodName.split("_");
        return methodNameParts[0];
    }
}

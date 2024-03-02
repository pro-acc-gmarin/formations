package user.infrastructure.dao;

import gateway.configuration.transaction.TransactionManager;
import user.infrastructure.entity.UserPersistence;
import user.infrastructure.helper.RoleEnum;
import user.infrastructure.spi.UserDaoSpi;
import utils.annotations.MeasurePerformance;
import utils.annotations.PreparedQuery;
import utils.annotations.Transactional;
import utils.helpers.PreparedQueryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class UserDao implements UserDaoSpi {

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("INSERT INTO user (id, firstname, lastname, email, password, role, salt) VALUES (?, ?, ?, ?, ?, ?, ?)")
    public UserPersistence add(final UserPersistence userPersistence) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), UserPersistence.class), Statement.RETURN_GENERATED_KEYS);
        userPersistence.setId(this.generateUUID());
        userPersistence.setRole(RoleEnum.STANDARD.name());
        statement.setString(1, userPersistence.getId());
        statement.setString(2, userPersistence.getFirstname());
        statement.setString(3, userPersistence.getLastname());
        statement.setString(4, userPersistence.getEmail());
        statement.setString(6, userPersistence.getRole());
        PreparedQueryHelper.setPasswordAndSaltWithIndex(statement, 5, 7);

        final int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Updating user failed, no rows affected.");
        }


        return userPersistence;
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("DELETE FROM user WHERE id = ?")
    public void delete(final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class), Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, id);

        final int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Deleting user failed, no rows affected.");
        }

    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("UPDATE user SET firstname = COALESCE(?, firstname), lastname = COALESCE(?, lastname), email = COALESCE(?, email) WHERE id = ?")
    public Optional<UserPersistence> update(final UserPersistence userPersistence, final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Optional<UserPersistence> oUserPersistence = this.getById(id);
        if (oUserPersistence.isPresent()) {
            final Connection connection = TransactionManager.getConnection();
            final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), UserPersistence.class, String.class), Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, userPersistence.getFirstname());
            statement.setString(2, userPersistence.getLastname());
            statement.setString(3, userPersistence.getEmail());
            statement.setString(4, id);

            final int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            return this.getById(id);
        } else {
            return ofNullable(this.add(userPersistence));
        }
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("SELECT id, firstname, lastname, email, role FROM user")
    public List<UserPersistence> getAll() throws SQLException, NoSuchMethodException {
        final List<UserPersistence> userPersistenceList = new ArrayList<>();
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithoutParameter(currentMethodName, this.getClass()));

        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            final UserPersistence userPersistence = new UserPersistence();
            userPersistence.setId(resultSet.getString("id"));
            userPersistence.setFirstname(resultSet.getString("firstname"));
            userPersistence.setLastname(resultSet.getString("lastname"));
            userPersistence.setEmail(resultSet.getString("email"));
            userPersistence.setRole(resultSet.getString("role"));
            userPersistenceList.add(userPersistence);
        }


        return userPersistenceList;
    }

    @Override
    @MeasurePerformance
    @Transactional
    @PreparedQuery("SELECT * FROM user WHERE id = ?")
    public Optional<UserPersistence> getById(final String id) throws SQLException, NoSuchMethodException {
        final String currentMethodName = this.getMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
        final Connection connection = TransactionManager.getConnection();
        final PreparedStatement statement = connection.prepareStatement(PreparedQueryHelper.getPreparedQueryValueWithParameter(currentMethodName, this.getClass(), String.class));
        statement.setString(1, id);

        final ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            final UserPersistence userPersistence = new UserPersistence();
            userPersistence.setId(resultSet.getString("id"));
            userPersistence.setFirstname(resultSet.getString("firstname"));
            userPersistence.setLastname(resultSet.getString("lastname"));
            userPersistence.setEmail(resultSet.getString("email"));
            userPersistence.setRole(resultSet.getString("role"));
            return of(userPersistence);
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

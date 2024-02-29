package user.infrastructure.spi;

import task.infrastructure.entity.TaskPersistence;
import user.infrastructure.entity.UserPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDaoSpi {
    UserPersistence add(UserPersistence object) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Optional<UserPersistence> update(UserPersistence object, String id) throws SQLException, NoSuchMethodException;

    List<UserPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<UserPersistence> getById(String id) throws SQLException, NoSuchMethodException;
}

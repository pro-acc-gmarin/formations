package user.infrastructure.spi;

import task.infrastructure.entity.TaskPersistence;
import user.infrastructure.entity.UserPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDaoSpi {
    UserPersistence add(final UserPersistence object) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Optional<UserPersistence> update(final UserPersistence object, final String id) throws SQLException, NoSuchMethodException;

    List<UserPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<UserPersistence> getById(final String id) throws SQLException, NoSuchMethodException;
}

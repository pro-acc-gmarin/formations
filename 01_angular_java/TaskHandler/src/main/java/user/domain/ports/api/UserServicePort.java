package user.domain.ports.api;

import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface UserServicePort {

    User add(final User user) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    User update(final User user, final String id) throws SQLException, NoSuchMethodException;

    List<User> getAll() throws SQLException, NoSuchMethodException;

    User getById(final String id) throws SQLException, NoSuchMethodException;

}

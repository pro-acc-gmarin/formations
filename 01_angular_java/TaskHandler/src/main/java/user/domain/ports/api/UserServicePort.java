package user.domain.ports.api;

import user.domain.data.User;

import java.sql.SQLException;
import java.util.List;

public interface UserServicePort {

    User add(User user) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    User update(User user, String id) throws SQLException, NoSuchMethodException;

    List<User> getAll() throws SQLException, NoSuchMethodException;

    User getById(String id) throws SQLException, NoSuchMethodException;

}

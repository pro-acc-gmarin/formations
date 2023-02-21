package user.domain.ports.api;

import user.domain.data.User;
import user.domain.ports.spi.UserPersistencePort;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserServicePort{

    private final UserPersistencePort userPersistencePort;

    public UserServiceImpl(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    public User add(User user) throws SQLException {
        return this.userPersistencePort.add(user);
    }

    public void delete(String id) {
        this.userPersistencePort.delete(id);
    }

    public User update(User user, String id) throws SQLException {
        return this.userPersistencePort.update(user, id);
    }

    public List<User> getAll() throws SQLException {
        return this.userPersistencePort.getAll();
    }

    public User getById(String id) {
        return this.userPersistencePort.getById(id);
    }
}

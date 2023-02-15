package user.domain.ports.api;

import user.domain.User;
import user.domain.ports.spi.UserPersistencePort;

import java.util.List;

public class UserServiceImpl implements UserServicePort{

    private final UserPersistencePort userPersistencePort;

    public UserServiceImpl(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    public User add(User user) {
        return this.userPersistencePort.add(user);
    }

    public void delete(Long id) {
        this.userPersistencePort.delete(id);
    }

    public User update(User user) {
        return this.userPersistencePort.update(user);
    }

    public List<User> getAll() {
        return this.userPersistencePort.getAll();
    }

    public User getById(Long id) {
        return this.userPersistencePort.getById(id);
    }
}

package user.domain.ports.spi;

import user.domain.User;

import java.util.List;

public interface UserPersistencePort {

    User add(User user);

    void delete(Long id);

    User update(User user);

    List<User> getAll();

    User getById(Long id);

}

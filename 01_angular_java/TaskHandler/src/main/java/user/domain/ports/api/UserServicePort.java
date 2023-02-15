package user.domain.ports.api;

import user.domain.User;

import java.util.List;

public interface UserServicePort {

    User add(User user);

    void delete(Long id);

    User update(User user);

    List<User> getAll();

    User getById(Long id);

}

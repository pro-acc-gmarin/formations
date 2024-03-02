package board.infrastructure.spi;

import board.infrastructure.entity.BoardPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BoardDaoSpi{
    BoardPersistence add(final BoardPersistence object) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Optional<BoardPersistence> update(final BoardPersistence object, final String id) throws SQLException, NoSuchMethodException;

    List<BoardPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<BoardPersistence> getById(final String id) throws SQLException, NoSuchMethodException;
}

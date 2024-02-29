package board.infrastructure.spi;

import board.infrastructure.entity.BoardPersistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BoardDaoSpi{
    BoardPersistence add(BoardPersistence object) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Optional<BoardPersistence> update(BoardPersistence object, String id) throws SQLException, NoSuchMethodException;

    List<BoardPersistence> getAll() throws SQLException, NoSuchMethodException;

    Optional<BoardPersistence> getById(String id) throws SQLException, NoSuchMethodException;
}

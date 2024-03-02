package board.domain.ports.spi;

import board.domain.data.Board;
import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface BoardPersistencePort {

    Board add(final Board board) throws SQLException, NoSuchMethodException;

    void delete(final String id) throws SQLException, NoSuchMethodException;

    Board update(final Board board, final String id) throws SQLException, NoSuchMethodException;

    List<Board> getAll() throws SQLException, NoSuchMethodException;

    Board getById(final String id) throws SQLException, NoSuchMethodException;

}

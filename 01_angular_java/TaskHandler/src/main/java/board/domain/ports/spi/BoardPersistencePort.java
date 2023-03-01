package board.domain.ports.spi;

import board.domain.data.Board;
import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface BoardPersistencePort {

    Board add(Board board) throws SQLException, NoSuchMethodException;

    void delete(String id) throws SQLException, NoSuchMethodException;

    Board update(Board board, String id) throws SQLException, NoSuchMethodException;

    List<Board> getAll() throws SQLException, NoSuchMethodException;

    Board getById(String id) throws SQLException, NoSuchMethodException;

}

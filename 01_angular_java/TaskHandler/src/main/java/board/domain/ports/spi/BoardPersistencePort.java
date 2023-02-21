package board.domain.ports.spi;

import board.domain.data.Board;
import task.domain.data.Task;

import java.sql.SQLException;
import java.util.List;

public interface BoardPersistencePort {

    Board add(Board board) throws SQLException;

    void delete(String id);

    Board update(Board board, String id) throws SQLException;

    List<Board> getAll() throws SQLException;

    Board getById(String id);

}

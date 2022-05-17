package server;

import utility.Answer;
import utility.Request;

import java.sql.SQLException;

public interface RequestHandlerStrategy {
    Answer handle(Request request) throws SQLException;

}

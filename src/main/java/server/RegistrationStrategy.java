package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.database.UserDatabase;
import utility.Answer;
import utility.AnswerType;
import utility.Request;
import utility.exceptions.AuthorizationException;

import java.sql.SQLException;

public class RegistrationStrategy implements RequestHandlerStrategy {
    private Logger log = LoggerFactory.getLogger(MainServer.class);

    @Override
    public Answer handle(Request request) throws SQLException {
        log.info("Регистрация пользователя: " + request.getUserData().getLogin());
        return executeRequest(request);
    }

    private Answer executeRequest(Request request) throws SQLException {
        Answer answer;
        try {
            UserDatabase.getInstance().insertElement(request.getUserData());
            answer = new Answer(AnswerType.SUCCESSFULLY, "");
        } catch ( AuthorizationException e) {
            answer = new Answer(AnswerType.UNSUCCESSFULLY, e.getMessage());
        }
        return answer;
    }
}
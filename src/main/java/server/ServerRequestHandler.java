package server;

import utility.*;
import server.commands.ActionInvoker;
import server.database.UserDatabase;
import utility.exceptions.*;
import utility.interaction.Command;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRequestHandler {
    private Logger log = LoggerFactory.getLogger(MainServer.class);
    private InteractionServer server;
    private ActionInvoker invoker;
    private UserDatabase userData;

    public ServerRequestHandler() throws SQLException {
        this.server = InteractionServer.getInstance();
        this.invoker = ActionInvoker.getInstance();
        this.userData = UserDatabase.getInstance();
        userData.createTableIfNotExist();
    }

    public void handle() throws SQLException {
        Answer answer;
        Request request = server.getRequest();
        switch (request.getRequestType()) {
            case COMMAND_EXECUTION:
                UserDatabase.getInstance().checkUserPassword(request.getUserData());
                Command command = request.getCommand();
                log.info("Обработка команды: " + command.getName());
                try {
                    invoker.setCurrentUser(request.getUserData());
                    answer = invoker.execute(command);
                } catch (IncorrectCommandException | IncorrectArgumentException | MissingElementException |
                         DatabaseElementError e) {
                    answer = new Answer(AnswerType.RETURN_ACTION, e.getMessage());
                    log.error("Команда " + command.getName() + " не выполнена. Ошибка: " + e.getMessage() + " Аргумент: " + command.getParameter());
                } catch (IllegalArgumentException e) {
                    answer = new Answer(AnswerType.RETURN_ACTION, "Аргумент команды некорректен!");
                    log.error("Аргумент команды " + command.getName() + " некорректен. Введено: " + command.getParameter());
                }
                if (answer.getType().toString().equals("EXIT")) {
                    log.debug("Клиентское приложение завершено. Коллекция сохранена");
                }
                server.sendAnswer(answer);
                break;
            case REGISTRATION_REQUEST:
                log.debug("Регистрация пользователя: " + request.getUserData().getLogin());
                try {
                    userData.insertElement(request.getUserData());
                    server.sendAnswer(new Answer(AnswerType.SUCCESSFULLY_AUTHORIZATION, ""));
                } catch (DatabaseElementError e) {
                    server.sendAnswer(new Answer(AnswerType.UNSUCCESSFULLY_AUTHORIZATION, e.getMessage()));
                }
                break;
            case AUTORIZATION_REQUEST:
                log.debug("Авторизация пользователя: " + request.getUserData().getLogin());
                try {
                    userData.checkUserPassword(request.getUserData());
                    server.sendAnswer(new Answer(AnswerType.SUCCESSFULLY_AUTHORIZATION, ""));
                } catch (DatabaseElementError e) {
                    server.sendAnswer(new Answer(AnswerType.UNSUCCESSFULLY_AUTHORIZATION, e.getMessage()));
                }
        }
    }
}

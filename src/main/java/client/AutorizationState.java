package client;

import utility.*;

import java.io.IOException;

public class AutorizationState implements InteractionStrategy{
    @Override
    public StateConfiguration handleAnswer(ConsoleReader reader, InteractionClient client) throws IOException {
        String login;
        String password;
        System.out.println("Авторизация:");
        System.out.println("Введите логин:");
        while (true) {
            login = reader.readLine();
            break;
        }
        System.out.println("Введите пароль:");
        while (true) {
            password = reader.readLine();
            break;
        }
        UserData userData = new UserData(login, password);
        client.setUserData(userData);
        Request request = new Request(RequestType.AUTORIZATION_REQUEST);
        client.sendRequest(request);
        Answer answer=client.getAnswer();
        if (answer.getType()== AnswerType.SUCCESSFULLY_AUTHORIZATION){
            return new StateConfiguration("Вы успешно авторизовались. Введите help для просмотра возможных команд!",new DialogStrategy());
        }
        else{
            return new StateConfiguration(answer.getMessage(),new AutorizationState());
        }

    }
}

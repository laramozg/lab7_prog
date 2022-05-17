package client;

import utility.exceptions.ExitException;


public class MainClient {
    public static void main(String[] args) {
        InteractionClient client = new InteractionClient("localhost", 3001);
        ConsoleReader reader = new ConsoleReader();
        InteractionManager manager = new InteractionManager(client, reader);
        System.out.println("Вы зарегистрированы?(Y/N)");
        while(true){
            String mode=reader.readLine().toLowerCase();
            if (mode.equals("y")){
                manager.setStrategy(new AutorizationState());
                break;
            }
            if (mode.equals("n")){
                manager.setStrategy(new RegistrationState());
                break;
            }
            System.out.println("Введено не требуемое значение");
        }
        while (true) {
            try {
                manager.handleAnswer();
            }
            catch (ExitException e){
                break;
            }
        }
    }
}

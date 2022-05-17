package client;

import utility.exceptions.IncorrectCommandException;

import java.io.IOException;

public class InteractionManager {

    InteractionStrategy strategy;
    InteractionClient client;
    ConsoleReader reader;

    public InteractionManager(InteractionClient client, ConsoleReader reader){
        this.client=client;
        this.reader=reader;
    }

    public void setStrategy(InteractionStrategy strategy) {
        this.strategy = strategy;
    }

    public void handleAnswer(){
        try {
            StateConfiguration info= strategy.handleAnswer(reader, client);
            if (!info.getMessage().equals("")){
                System.out.println(info.getMessage());
            }
            setStrategy(info.getStrategy());
        } catch (IOException e) {
            System.out.println("Сервер временно недоступен.....");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IncorrectCommandException e) {
            System.out.println(e.getMessage());
        }
    }
}

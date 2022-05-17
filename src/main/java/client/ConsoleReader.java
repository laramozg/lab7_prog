package client;


import utility.element.Worker;
import utility.interaction.Command;
import utility.interaction.Input;

import java.util.Scanner;

/**
 * Класс получения и анализа данных из командной строки
 */
public class ConsoleReader {

    private Input input;


    public ConsoleReader() {
        this.input=new ConsoleInput(new Scanner(System.in));
    }

    public String readLine(){
        return input.getReader().nextLine().trim();
    }

    public Command readCommand(){return input.readCommand();}

    public Worker readElement() {
        return input.readElement();
    }
}

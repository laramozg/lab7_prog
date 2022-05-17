package utility.exceptions;

public class DatabaseElementError extends RuntimeException{
    public DatabaseElementError(String message){
        super(message);
    }
}

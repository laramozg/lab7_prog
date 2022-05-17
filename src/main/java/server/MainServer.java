package server;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainServer {
    public static void main(String[] args) throws SQLException {
        Logger log = LoggerFactory.getLogger(MainServer.class);
        ServerRequestHandler handler = new ServerRequestHandler();
        System.out.println("Сервер запущен!");
        log.debug("Сервер запущен!");
        while (true) {
            handler.handle();
        }
    }
}

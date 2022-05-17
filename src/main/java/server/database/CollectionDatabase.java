package server.database;


import utility.*;
import utility.element.*;
import utility.exceptions.DatabaseElementError;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

public class CollectionDatabase {
    private static CollectionDatabase instance = null;
    private Connection connection;
    private static final String DB_URL = "jdbc:postgresql://localhost:63333/studs";
    private static final String USER = "s336758";
    private static final String PASS = "oqv620";
    private static final String INSERT_VALUE = "INSERT INTO " +
            "worker(keys,name,x,y,creationDate,salary,startDate,position ,status,employeesCount,type,postalAddress,creator)" +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_BY_KEY = "DELETE FROM worker WHERE keys=?";
    private static final String CLEAR = "DELETE FROM worker WHERE creator=?";
    private static final String REMOVE_GREATER = "DELETE FROM worker WHERE employeesCount>? and creator=?";
    private static final String UPDATE_ELEMENT_BY_ID = "UPDATE worker SET " +
            "name=?,x=?,y=?,salary=?,startDate=?,position=?,status=?,employeesCount=?,type=?,postalAddress=? " +
            "WHERE id=?";
    private static final String REPLACE_IF_GREATER = "UPDATE worker SET " +
            "name=?,x=?,y=?,salary=?,startDate=?,position=?,status=?,employeesCount=?,type=?,postalAddress=? " +
            "WHERE keys=? and employeesCount>?";
    private static final String REPLACE_IF_LOWE = "UPDATE worker SET " +
            "name=?,x=?,y=?,salary=?,startDate=?,position=?,status=?,employeesCount=?,type=?,postalAddress=? " +
            "WHERE keys=? and employeesCount<?";
    private static final String SELECT_ELEMENTS = "SELECT * FROM worker";

    private static final String GET_ELEMENT_BY_ID = "SELECT * FROM worker WHERE id=?";
    private static final String GET_ELEMENT_BY_KEY = "SELECT * FROM worker WHERE keys=?";

    private CollectionDatabase() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static CollectionDatabase getInstance() {
        if (instance == null) {
            try {
                instance = new CollectionDatabase();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public void createTableIfNotExist() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS worker(" +
                "keys VARCHAR(50)," +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(50)," +
                "x DOUBLE ," +
                "y DOUBLE PRECISION ," +
                "creationDate VARCHAR(50)," +
                "salary FLOAT NULL," +
                "startDate VARCHAR(50)," +
                "position VARCHAR(50) NULL," +
                "status VARCHAR(50) NULL," +
                "employeesCount INTEGER ," +
                "type VARCHAR(50)," +
                "postalAddress VARCHAR(50)," +
                "creator VARCHAR(50)" +
                ")";
        connection.createStatement().execute(sql);
    }

    public Worker insertElement(String key, Worker worker) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_VALUE, Statement.RETURN_GENERATED_KEYS);
        worker.setCreationDate(LocalDate.now().toString());
        statement.setString(1, key);
        statement.setString(2, worker.getName());
        statement.setDouble(3, worker.getCoordinates().getX());
        statement.setDouble(4, worker.getCoordinates().getY());
        statement.setString(5, worker.getCreationDate());
        try {
            statement.setFloat(6, worker.getSalary());
        } catch (NullPointerException e) {
            statement.setString(6, null);
        }
        statement.setString(7, worker.getStartDate().toString());
        try {
            statement.setString(8, worker.getPosition().toString());
        } catch (NullPointerException e) {
            statement.setString(8, null);
        }
        try {
            statement.setString(9, worker.getStatus().toString());
        } catch (NullPointerException e) {
            statement.setString(9, null);
        }
        statement.setLong(10, worker.getOrganization().getEmployeesCount());
        statement.setString(11, worker.getOrganization().getType().toString());
        statement.setString(12, worker.getOrganization().getPostalAddress().toString());
        statement.setString(13, worker.getCreator());
        statement.executeUpdate();
        ResultSet newData = statement.getGeneratedKeys();
        while (newData.next()) {
            worker.setId(newData.getInt(2));
            break;
        }
        return worker;

    }

    public Hashtable<String,Worker> getData() {
        Hashtable<String,Worker> data = new Hashtable<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(SELECT_ELEMENTS);
            while (set.next()) {
                String key = set.getString(1);
                int id = set.getInt(2);
                String name = set.getString(3);
                Coordinates coordinates = new Coordinates(set.getDouble(4), set.getDouble(5));
                String creationDate = set.getString(6);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date startDate = simpleDateFormat.parse(set.getString(8));
                Float salary;
                Position position;
                Status status;
                Organization organization;
                try {
                    salary = Float.valueOf(set.getString(7));
                } catch (NullPointerException e) {
                    salary = null;
                }
                try {
                    position = Position.valueOf(set.getString(9));
                } catch (NullPointerException e) {
                    position = null;
                }
                try {
                    status = Status.valueOf(set.getString(10));
                } catch (NullPointerException e) {
                    status = null;
                }
                organization = new Organization(set.getLong(11),OrganizationType.valueOf(set.getString(12)),new Address(set.getString(13)));
                String creator=set.getString(14);
                data.put(key,new Worker(id, name, coordinates, creationDate, salary, startDate, position, status, organization, creator));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void updateElement(int id, Worker worker) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_ELEMENT_BY_ID);
            statement.setString(1, worker.getName());
            statement.setDouble(2, worker.getCoordinates().getX());
            statement.setDouble(3, worker.getCoordinates().getY());
            try {
                statement.setFloat(4, worker.getSalary());
            } catch (NullPointerException e) {
                statement.setString(4, null);
            }
            statement.setString(5, worker.getStartDate().toString());
            try {
                statement.setString(6, worker.getPosition().toString());
            } catch (NullPointerException e) {
                statement.setString(6, null);
            }
            try {
                statement.setString(7, worker.getStatus().toString());
            } catch (NullPointerException e) {
                statement.setString(7, null);
            }
            statement.setLong(8, worker.getOrganization().getEmployeesCount());
            statement.setString(9, worker.getOrganization().getType().toString());
            statement.setString(10, worker.getOrganization().getPostalAddress().toString());
            statement.setInt(11, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByKey(String key) {
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_KEY);
            statement.setString(1, key);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceIfGreater(String key,Worker worker) {
        try {
            PreparedStatement statement = connection.prepareStatement(REPLACE_IF_GREATER);
            statement.setString(1, worker.getName());
            statement.setDouble(2, worker.getCoordinates().getX());
            statement.setDouble(3, worker.getCoordinates().getY());
            try {
                statement.setFloat(4, worker.getSalary());
            } catch (NullPointerException e) {
                statement.setString(4, null);
            }
            statement.setString(5, worker.getStartDate().toString());
            try {
                statement.setString(6, worker.getPosition().toString());
            } catch (NullPointerException e) {
                statement.setString(6, null);
            }
            try {
                statement.setString(7, worker.getStatus().toString());
            } catch (NullPointerException e) {
                statement.setString(7, null);
            }
            statement.setLong(8, worker.getOrganization().getEmployeesCount());
            statement.setString(9, worker.getOrganization().getType().toString());
            statement.setString(10, worker.getOrganization().getPostalAddress().toString());
            statement.setString(11,key);
            statement.setLong(12, worker.getOrganization().getEmployeesCount());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void replaceIfLowe(String key,Worker worker) {
        try {
            PreparedStatement statement = connection.prepareStatement(REPLACE_IF_LOWE);
            statement.setString(1, worker.getName());
            statement.setDouble(2, worker.getCoordinates().getX());
            statement.setDouble(3, worker.getCoordinates().getY());
            try {
                statement.setFloat(4, worker.getSalary());
            } catch (NullPointerException e) {
                statement.setString(4, null);
            }
            statement.setString(5, worker.getStartDate().toString());
            try {
                statement.setString(6, worker.getPosition().toString());
            } catch (NullPointerException e) {
                statement.setString(6, null);
            }
            try {
                statement.setString(7, worker.getStatus().toString());
            } catch (NullPointerException e) {
                statement.setString(7, null);
            }
            statement.setLong(8, worker.getOrganization().getEmployeesCount());
            statement.setString(9, worker.getOrganization().getType().toString());
            statement.setString(10, worker.getOrganization().getPostalAddress().toString());
            statement.setString(11,key);
            statement.setLong(12, worker.getOrganization().getEmployeesCount());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear(UserData userData) {
        try {
            PreparedStatement statement = connection.prepareStatement(CLEAR);
            statement.setString(1, userData.getLogin());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeGreater(long employeesCount, UserData userData) {
        try {
            PreparedStatement statement = connection.prepareStatement(REMOVE_GREATER);
            statement.setLong(1, employeesCount);
            statement.setString(2, userData.getLogin());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void checkElementAccess(int id, UserData userData) {
        String creator = getCreatorById(id);
        if (!userData.getLogin().equals(creator)) {
            throw new DatabaseElementError("Этот элемент Вам не принадлежит!");
        }
    }

    public void checkElementAccess(String key, UserData userData) {
        String creator = getCreatorByKey(key);
        if (!userData.getLogin().equals(creator)) {
            throw new DatabaseElementError("Этот элемент Вам не принадлежит!");
        }
    }

    private String getCreatorById(int id) {
        String creator;
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ELEMENT_BY_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            creator = set.getString(14);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return creator;
    }
    private String getCreatorByKey(String key) {
        String creator;
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ELEMENT_BY_KEY);
            statement.setString(1, key);
            ResultSet set = statement.executeQuery();
            set.next();
            creator = set.getString(14);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return creator;
    }

}

package org.example.managers;

import org.example.data.*;
import org.example.util.Printable;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBManager {
    private Printable console;
    private Connection connection;
    private CollectionManager collectionManager;
    private Statement statement;
    private String dbURL = "jdbc:postgresql://localhost:5433/studs";

    public DBManager(Printable console, CollectionManager collectionManager) {
        this.console = console;
        this.collectionManager = collectionManager;
    }

    public void connect() {
        Properties propsInfo = new Properties();
        try {
            Class.forName("org.postgresql.Driver");
            propsInfo.load(new FileInputStream("DBInfo.cfg"));
            connection = DriverManager.getConnection(dbURL, propsInfo);
            statement = connection.createStatement();
            console.println("Подключение к БД успешно!");
        } catch (ClassNotFoundException e) {
            console.printError("не получилось найти драйвер((");
        } catch (SQLException e) {
            console.printError("непредвиденная ошибка с БД");
        } catch (IOException e) {
            console.printError("не получилось найти файл конфигурации");
        } catch (RuntimeException e) {
            console.printError("ошибка во время исполнения");
        }
    }

    public void clearDB() throws SQLException {
        ResultSet result = statement.executeQuery("SELECT delete_all_person_data();");
        console.println("удаление произошло успешно!");
    }

    public boolean addPerson(Person person, String userName, String userPassword) {
        String query = "SELECT add_person(?, ?::INTEGER, ?::REAL, ?::REAL, ?, CAST(? AS hair_colour), " +
                "CAST(? AS nationality), ?::REAL, ?::BIGINT, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, person.getName()); // person_name -> TEXT
            preparedStatement.setInt(2, person.getCoordinates().getX()); // coordinate_x -> INTEGER
            preparedStatement.setFloat(3, person.getCoordinates().getY()); // coordinate_y -> FLOAT
            preparedStatement.setFloat(4, person.getHeight()); // person_height -> FLOAT
            preparedStatement.setString(5, person.getPassportID()); // person_passport_id -> TEXT
            preparedStatement.setString(6, person.getHairColor().toString()); // person_hair -> hair_colour (as TEXT in Java)
            preparedStatement.setString(7, person.getNationality().toString()); // person_nationality -> nationality (as TEXT in Java)
            preparedStatement.setFloat(8, person.getLocation().getX()); // location_x -> FLOAT
            preparedStatement.setLong(9, person.getLocation().getY()); // location_y -> BIGINT
            preparedStatement.setString(10, person.getLocation().getName()); // location_name -> TEXT
            preparedStatement.setString(11, userName); // user_name -> TEXT
            preparedStatement.setString(12, userPassword); // user_password -> TEXT

            // Выполнение запроса и получение возвращаемого значения
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getBoolean(1)) {
                // Запрос успешен и функция вернула TRUE
                console.println("Person successfully added to the database.");
                return true;
            } else {
                // Запрос выполнен, но функция вернула FALSE
                console.printError("Failed to add person to the database.");
                return false;
            }
        } catch (SQLException e) {
            console.printError("ошибка нахой");
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            System.out.println("Invalid login: " + login);
            return false;
        }

        console.println(login);
        String query = "SELECT check_login(?) AS login_valid;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Проверяем, действительно ли колонка login_valid существует в ResultSet
                boolean result = resultSet.getBoolean("login_valid");
                System.out.println(login + " login valid: " + result);
                return result;
            } else {
                System.out.println("No result returned from database for login: " + login);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Exception occurred: " + e.getMessage());
            return false;
        }
    }


    public boolean checkUser(String userName, String password) {
        String query = "SELECT check_user(?, ?);";
        console.println(userName + "   " + password);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName.trim());
            statement.setString(2, password.trim());

            ResultSet resultSet = statement.executeQuery();

            // Проверяем, есть ли результат
            if (resultSet.next()) {
                // Возвращаем результат вызова функции
                return resultSet.getBoolean(1);
            } else {
                // Если нет результата, возвращаем false или можно выбросить исключение
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeHead() throws SQLException {
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT delete_first_person();");
    }

    public void updateID(int oldID, int newID) {
        try {
            String query = "SELECT * FROM update_person_id(?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, oldID);
                preparedStatement.setInt(2, newID);

                preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
        }
    }

    public boolean createUser(String userName, String userPassword) {
        // SQL для вызова функции add_user
        String sql = "{ ? = call add_user(?, ?) }";

        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
            callableStatement.registerOutParameter(1, java.sql.Types.BOOLEAN);
            callableStatement.setString(2, userName);
            callableStatement.setString(3, userPassword);
            callableStatement.execute();
            return callableStatement.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeByID(String username, long id) {
        String query = "SELECT delete_person_by_id(?, ?);";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String result = resultSet.getString(1);
                System.out.println(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public Person pushInCollection() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM get_all_person_info();");
            while (result.next()) {
                int id = result.getInt("person_id"); //        person_id INTEGER,
                String name = result.getString("person_name"); //        person_name TEXT,
                int coordinatesX = result.getInt("coordinate_x");//        coordinate_x INTEGER,
                float coordinatesY = result.getFloat("coordinate_y");//        coordinate_y FLOAT,
                float height = result.getFloat("person_height");//        person_height FLOAT,
                String passportID = result.getString("person_passport_id");//        person_passport_id TEXT,
                Color hair = Color.valueOf(result.getString("person_hair"));//        person_hair hair_colour,
                Country nationality = Country.valueOf(result.getString("person_nationality"));//        person_nationality nationality,
                Float locationX = result.getFloat("location_x");//        location_x FLOAT,
                long locationY = result.getLong("location_y");//        location_y BIGINT,
                String locationName = result.getString("location_name");//        location_name TEXT,
//                console.println(id + " " + name + " " + coordinatesX + " " + coordinatesY + " " + height
//                        + " " + passportID + " " + hair + " " + nationality + " " + locationX + " " + locationY
//                        + " " + locationName);
                Person person = new Person(id, name, new Coordinates(coordinatesX, coordinatesY), height, passportID, hair, nationality, new Location(locationX, locationY, locationName));
                collectionManager.setIdCounter(person.getID());
                collectionManager.addElement(person);
            }
        } catch (SQLException e) {
            console.printError("не предвиденная ошибка с БД");
            throw new RuntimeException(e);

        }
        return null;
    }
}

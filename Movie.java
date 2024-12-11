package ys;

import java.sql.*;
import java.util.Scanner;

public class Movie {

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ys"; // Update with your DB details
    private static final String USER = "root"; // Update with your DB username
    private static final String PASS = ""; // Update with your DB password

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // Create the Movies table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Movies (" +
                                      "ID INT PRIMARY KEY, " +
                                      "Movie_Name VARCHAR(100), " +
                                      "Genre VARCHAR(50), " +
                                      "IMDB_Rating FLOAT, " +
                                      "Year INT)";
            statement.execute(createTableQuery);
            System.out.println("Table 'Movies' created or already exists.");

            // Insert sample data into the Movies table
            String insertSampleData = "INSERT IGNORE INTO Movies (ID, Movie_Name, Genre, IMDB_Rating, Year) VALUES " +
                                      "(1, 'Inception', 'Sci-Fi', 8.8, 2010), " +
                                      "(2, 'The Dark Knight', 'Action', 9.0, 2008), " +
                                      "(3, 'Interstellar', 'Sci-Fi', 8.6, 2014), " +
                                      "(4, 'The Godfather', 'Crime', 9.2, 1972), " +
                                      "(5, 'Pulp Fiction', 'Crime', 8.9, 1994), " +
                                      "(6, 'The Shawshank Redemption', 'Drama', 9.3, 1994), " +
                                      "(7, 'The Matrix', 'Sci-Fi', 8.7, 1999), " +
                                      "(8, 'Avengers: Endgame', 'Action', 8.4, 2019), " +
                                      "(9, 'Forrest Gump', 'Drama', 8.8, 1994), " +
                                      "(10, 'Gladiator', 'Action', 8.5, 2000)";
            statement.execute(insertSampleData);
            System.out.println("Sample data inserted into 'Movies' table.");

            // i. Display details of all the Movies from the table
            String queryAllMovies = "SELECT * FROM Movies";
            resultSet = statement.executeQuery(queryAllMovies);

            System.out.println("\nAll Movies:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("ID") +
                                   ", Movie Name: " + resultSet.getString("Movie_Name") +
                                   ", Genre: " + resultSet.getString("Genre") +
                                   ", IMDB Rating: " + resultSet.getFloat("IMDB_Rating") +
                                   ", Year: " + resultSet.getInt("Year"));
            }

            // ii. Display details of the 5th Movie from the table
            resultSet.absolute(5);  // Move the cursor to the 5th row
            if (resultSet.isBeforeFirst() || resultSet.isAfterLast()) {
                System.out.println("\nThere is no 5th movie in the table.");
            } else {
                System.out.println("\n5th Movie:");
                System.out.println("ID: " + resultSet.getInt("ID") +
                                   ", Movie Name: " + resultSet.getString("Movie_Name") +
                                   ", Genre: " + resultSet.getString("Genre") +
                                   ", IMDB Rating: " + resultSet.getFloat("IMDB_Rating") +
                                   ", Year: " + resultSet.getInt("Year"));
            }

            // iii. Insert a new row into the table using PreparedStatement and display all the details
            Scanner scanner = new Scanner(System.in);

            System.out.print("\nEnter Movie ID: ");
            int newID = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character
            System.out.print("Enter Movie Name: ");
            String newMovieName = scanner.nextLine();
            System.out.print("Enter Genre: ");
            String newGenre = scanner.nextLine();
            System.out.print("Enter IMDB Rating: ");
            float newIMDBRating = scanner.nextFloat();
            System.out.print("Enter Year: ");
            int newYear = scanner.nextInt();

            String insertQuery = "INSERT INTO Movies (ID, Movie_Name, Genre, IMDB_Rating, Year) " +
                                 "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, newID);
            preparedStatement.setString(2, newMovieName);
            preparedStatement.setString(3, newGenre);
            preparedStatement.setFloat(4, newIMDBRating);
            preparedStatement.setInt(5, newYear);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("\nNew movie inserted successfully.");
            } else {
                System.out.println("Failed to insert the new movie.");
            }

            // Display all movies after insertion
            resultSet = statement.executeQuery(queryAllMovies);
            System.out.println("\nUpdated List of Movies:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("ID") +
                                   ", Movie Name: " + resultSet.getString("Movie_Name") +
                                   ", Genre: " + resultSet.getString("Genre") +
                                   ", IMDB Rating: " + resultSet.getFloat("IMDB_Rating") +
                                   ", Year: " + resultSet.getInt("Year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

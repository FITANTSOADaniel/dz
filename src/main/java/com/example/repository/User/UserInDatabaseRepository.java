package com.example.repository.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.Qualifier.PostgresQualifier;
import com.example.config.DatabaseConfig;
import com.example.models.Entity.User;

import jakarta.enterprise.context.ApplicationScoped;

@PostgresQualifier 
@ApplicationScoped 
public class UserInDatabaseRepository implements UserRepository{
    
    DataSource ds = DatabaseConfig.getDataSource();

    @Override
    public User save(User user){
        String sql = "INSERT INTO users (firstname, name, phone, balance) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPhone());
            statement.setBigDecimal(4, user.getBalance());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur", e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT id, firstname, name, phone, balance FROM users ORDER by id";

        List<User> users = new ArrayList<>();
        
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) { 
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getBigDecimal("balance")
                );
                users.add(user);
            }
    
        return users;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    @Override
    public Optional<User> findById(Integer id) {
        String sql = "SELECT id, firstname, name, phone, balance FROM users WHERE id = ?";

        try (Connection connection = ds.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
                if (statement.executeQuery().next()) {

                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getBigDecimal("balance")
                );
                return Optional.of(user);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    @Override
    public void deleteUser(String id){
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = ds.getConnection()) {
            
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, Integer.parseInt(id));

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Utilisateur introuvable avec l'id : " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException( "Erreur lors de la suppression de l'utilisateur", e );
        }
    }

    @Override
    public User updateUser(User user, String id){
        String sql = "UPDATE users SET firstname = ?, name = ?, phone = ?, balance = ? WHERE id = ?";
        
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPhone());
            statement.setBigDecimal(4, user.getBalance());
            statement.setInt(5, Integer.parseInt(id));

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) { throw new RuntimeException("Utilisateur introuvable avec l'id : " + id);}

            return user;

        } catch (SQLException e) {
            throw new RuntimeException( "Erreur lors de la modification de l'utilisateur : "+e, e );
        }
    }
}

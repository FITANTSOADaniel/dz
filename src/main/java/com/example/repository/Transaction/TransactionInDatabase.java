package com.example.repository.Transaction;

import com.example.config.DatabaseConfig;
import com.example.models.Entity.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TransactionInDatabase implements TransactionRepository{

    DataSource ds = DatabaseConfig.getDataSource();

    @Override
    public Transaction save(Transaction transaction){

        String debitSql = "UPDATE users SET balance = balance - ? WHERE id = ? AND balance >= ?";

        String creditSql = "UPDATE users SET balance = balance + ? WHERE id = ?";

        String transactionSql = "INSERT INTO transactions ( from_user_id, to_user_id, amount) VALUES (?, ?, ?)";

        try (Connection connection = ds.getConnection()) {

            // Désactiver l'auto-commit
            connection.setAutoCommit(false);

            try (
                PreparedStatement debit = connection.prepareStatement(debitSql);

                PreparedStatement credit = connection.prepareStatement(creditSql);

                PreparedStatement transactionSQL = connection.prepareStatement(transactionSql)
            ) {

                // 1. Retirer l'argent
                debit.setBigDecimal(1, transaction.getAmount());
                debit.setBigDecimal(3, transaction.getAmount());

                int debitRows = debit.executeUpdate();

                if (debitRows == 0) {
                    throw new RuntimeException(
                        "Solde insuffisant ou utilisateur inexistant"
                    );
                }

                // 2. Ajouter l'argent
                credit.setBigDecimal(1, transaction.getAmount());

                int creditRows = credit.executeUpdate();

                if (creditRows == 0) {
                    throw new RuntimeException(
                        "Utilisateur destinataire inexistant"
                    );
                }

                // 3. Enregistrer la transaction
                transactionSQL.setBigDecimal(3, transaction.getAmount());

                transactionSQL.executeUpdate();

                // Tout s'est bien passé
                connection.commit();

            } catch (Exception e) {
                // Une opération a échoué
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erreur lors du transfert",
                e
            );
        }
        return transaction;
    }
}

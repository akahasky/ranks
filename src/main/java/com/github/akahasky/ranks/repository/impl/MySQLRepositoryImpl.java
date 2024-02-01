package com.github.akahasky.ranks.repository.impl;

import com.github.akahasky.ranks.controller.RankController;
import com.github.akahasky.ranks.model.Rank;
import com.github.akahasky.ranks.repository.IRepository;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;

public class MySQLRepositoryImpl implements IRepository {

    private final RankController rankController;
    private Connection connection;

    public MySQLRepositoryImpl(RankController rankController, ConfigurationSection configurationSection) {

        this.rankController = rankController;

        String host = configurationSection.getString("host");
        String database = configurationSection.getString("database");
        String user = configurationSection.getString("username");
        String password = configurationSection.getString("password");

        String url = String.format("jdbc:mysql://%s/%s", host, database);

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Connection");
            connection = DriverManager.getConnection(url, user, password);

            init();

        }

        catch (Exception exception) { exception.printStackTrace(); }

    }

    @Override
    public void init() {

        try (PreparedStatement statement = connection.prepareStatement("create table if not exists playerRanks (name varchar(16) primary key not null, rankName varchar(16) not null);")) {

            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    @Override
    public void shutdown() {

        try {

            if (connection == null || connection.isClosed()) return;

            connection.close();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

    @Override
    public Rank selectOne(String name) {

        try (PreparedStatement statement = connection.prepareStatement("select * from playerRanks where name=?;")) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) return null;

                return rankController.get(resultSet.getString("rankName"));

            }

        }

        catch (SQLException exception) { exception.printStackTrace(); }

        return null;

    }

    @Override
    public void insertOne(String name, String rankName) {

        try (PreparedStatement statement = connection.prepareStatement("insert into playerRanks (name, rankName) values(?,?) on duplicate key update rankName=values(rankName);")) {

            statement.setString(1, name);
            statement.setString(2, rankName);

            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

}

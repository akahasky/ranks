package com.github.akahasky.ranks.repository.impl;

import com.github.akahasky.ranks.controller.RankController;
import com.github.akahasky.ranks.model.Rank;
import com.github.akahasky.ranks.repository.IRepository;

import java.io.File;
import java.sql.*;

public class SQLiteRepositoryImpl implements IRepository {

    private final RankController rankController;
    private Connection connection;

    public SQLiteRepositoryImpl(RankController rankController, File folder) {

        this.rankController = rankController;

        if (!folder.exists()) folder.mkdirs();

        File file = folder.toPath().resolve("database.sql").toFile();

        try {

            if (!file.exists()) file.createNewFile();

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());

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

        try (PreparedStatement statement = connection.prepareStatement("replace into playerRanks (name, rankName) values(?,?);")) {

            statement.setString(1, name);
            statement.setString(2, rankName);

            statement.executeUpdate();

        }

        catch (SQLException exception) { exception.printStackTrace(); }

    }

}

package songbook;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by pwilkin on 28-Mar-19.
 */
public class DatabaseConnection {

    public static interface DatabaseRunnable {
        public void execute(Connection con) throws SQLException;
    }

    public static interface ORMRunnable {
        public void execute(EntityManager manager);
    }

    private static DatabaseConnection singleton;

    private DatabaseConnection() {}

    public synchronized static DatabaseConnection getInstance() {
        if (singleton == null) {
            singleton = new DatabaseConnection();
        }
        return singleton;
    }

    public void runInTransaction(DatabaseRunnable dr) {
        String url = getJDBCUrl();
        try (Connection c = DriverManager.getConnection(url, "SA", "")) {
            c.setAutoCommit(false); // POCZĄTEK TRANSAKCJI
            try {
                dr.execute(c);
                c.commit(); // ZATWIERDZENIE TRANSAKCJI; c.rollback() <= cofnięcie
            } catch (Exception e) {
                c.rollback();
                throw e; // WYSTĄPIŁ BŁĄD, WIEC COFAMY
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getJDBCUrl() {
        String userHomeDir = System.getProperty("user.home");
        Path homeDir = Paths.get(userHomeDir);
        Path subdir = homeDir.resolve(".songbook");
        Path songsFile = subdir.resolve("songs.db");
        return "jdbc:hsqldb:file:" + songsFile.toAbsolutePath().toString();
    }

    public void initializeDatabaseIfNeeded() throws Exception {
        runInTransaction(con -> {
            ResultSet tbl = con.getMetaData().getTables(null, null, "ARTISTS", null);
            if (!tbl.first()) {
                con.createStatement().execute("CREATE TABLE ARTISTS (ID INT PRIMARY KEY IDENTITY, NAME VARCHAR(255))");
                con.createStatement().execute("CREATE TABLE ALBUMS (ID INT PRIMARY KEY IDENTITY, ARTIST INT, NAME VARCHAR(255))");
                con.createStatement().execute("CREATE TABLE SONGS (ID INT PRIMARY KEY IDENTITY, ALBUM INT, TITLE VARCHAR(255), LYRICS LONGVARCHAR)");
            }
        });
    }

    private EntityManager manager;

    public void runInORM(ORMRunnable runnable) {
        initializeManagerIfNeeded();
        runnable.execute(manager);
    }

    private synchronized void initializeManagerIfNeeded() {
        if (manager == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("javax.persistence.jdbc.url", getJDBCUrl());
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("songbook", params);
            manager = emf.createEntityManager();
        }
    }

}

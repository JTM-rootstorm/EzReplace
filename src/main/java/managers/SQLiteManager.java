package managers;

import org.jetbrains.annotations.NotNull;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.List;

public class SQLiteManager {
    private Connection assetConnection;
    private Connection locationConnection;

    private static SQLiteManager instance = null;

    private SQLiteManager() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        assetConnection = connectToAssetDB();
        generateDeviceTable();
        locationConnection = connectToLocationDB();
        generateLocationTable();
    }

    private Connection connectToAssetDB() {
        String dbString = "jdbc:sqlite::memory:";

        return connect(dbString);
    }

    private Connection connectToLocationDB() {
        String dbString = "jdbc:sqlite::memory:";

        return connect(dbString);
    }

    private Connection connect(String dbString) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(dbString);
            if (conn == null) {
                throw new SQLException();
            }
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return conn;
    }

    private void generateDeviceTable() {
        try (Statement statement = assetConnection.createStatement()) {
            statement.executeUpdate("drop table if exists DEVICES");
            statement.executeUpdate("create table DEVICES(barcode TEXT, asset INTEGER NOT NULL, serial TEXT, " +
                    "purchase_date TEXT, description TEXT, po_number TEXT, primary key (asset))");
            assetConnection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void generateLocationTable() {
        try (Statement statement = locationConnection.createStatement()) {
            statement.executeUpdate("drop table if exists LOCATIONS");
            statement.executeUpdate("create table LOCATIONS(location_code TEXT, location_name TEXT, " +
                    "primary key (location_code))");
            locationConnection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public CachedRowSet selectByBarcode(@NotNull String barcode) {
        return select("barcode", barcode.trim());
    }

    public CachedRowSet selectBySerial(@NotNull String serial) {
        return select("serial", serial.trim());
    }

    private CachedRowSet select(String selector, @NotNull String value) {
        String sql = "select * from DEVICES where " + selector + " = '" + value + "'";
        ResultSet resultSet;

        CachedRowSet cachedRowSet = null;

        try (Statement statement = assetConnection.createStatement()){
            RowSetFactory factory = RowSetProvider.newFactory();
            cachedRowSet = factory.createCachedRowSet();
            resultSet = statement.executeQuery(sql);

            cachedRowSet.populate(resultSet);
            cachedRowSet.next();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return cachedRowSet;
    }

    @SuppressWarnings("SqlResolve")
    public String selectFromLocationDB(String code) {
        if (code == null || code.isEmpty()) {
            return "";
        }

        String sql = "select location_name from LOCATIONS where location_code = '" + code.trim() + "'";

        try (Statement statement = locationConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
             ResultSet resultSet = statement.executeQuery(sql)){

            return resultSet.getString("location_name");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return "";
    }

    @SuppressWarnings("SqlResolve")
    public void loadLocationsToMemory(@NotNull List<List<String>> locationCSV) {
        try {
            PreparedStatement statement = locationConnection.prepareStatement(
                    "insert into LOCATIONS (location_code, location_name) values (?,?)");
            for (List<String> item : locationCSV) {
                statement.setString(1, item.get(0));
                statement.setString(2, item.get(1));
                statement.executeUpdate();
            }
            locationConnection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadAssetsToDB(@NotNull List<List<String>> assetList) {
        try {
            PreparedStatement statement = assetConnection.prepareStatement(
                    "insert or replace into DEVICES(barcode, asset, serial, purchase_date, description, po_number) " +
                            "values (?,?,?,?,?,?)");

            for (List<String> asset : assetList) {
                statement.setString(1, asset.get(0));
                statement.setString(2, asset.get(1));
                statement.setString(3, asset.get(2));
                statement.setString(4, asset.get(3));
                statement.setString(5, asset.get(4));
                statement.setString(6, asset.get(5));

                statement.executeUpdate();
            }
            assetConnection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static SQLiteManager getInstance() {
        if (instance == null) {
            instance = new SQLiteManager();
        }

        return instance;
    }
}

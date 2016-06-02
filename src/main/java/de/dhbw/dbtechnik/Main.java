package de.dhbw.dbtechnik;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;

public class Main {

	static {
		Logging.setLoggingDefaults(LogLevel.DEBUG, "[%-5p; %c{1}::%M] %m%n");
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println(
					"Supply arguments jdbc-url user password (e.g., 'jdbc:mysql://localhost/beispieldatenbank?verifyServerCertificate=false' 'root' 'pass'");
			System.exit(1);
		}

		String url = args[0];
		String user = args[1];
		String pw = args[2];

		new Thread(() -> {
			try {
				Connection databaseConnection1 = DriverManager.getConnection(url, user, pw);
				databaseConnection1.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				databaseConnection1.setAutoCommit(false);
				Statement neuesStatementCon1 = databaseConnection1.createStatement();

				ResultSet resultSet = neuesStatementCon1.executeQuery("SELECT Preis FROM Artikel WHERE Artikelnummer ='A1';");
				resultSet.next();
				int preis = resultSet.getInt(1);
				System.out.println("1: Preis = " + preis);
				Thread.sleep(2000);
				neuesStatementCon1.executeUpdate("UPDATE Artikel set Preis=" + (preis + 100) + " where Artikelnummer='A1';");

				databaseConnection1.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				Connection databaseConnection2 = DriverManager.getConnection(url, user, pw);
				databaseConnection2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				databaseConnection2.setAutoCommit(false);
				Statement neuesStatementCon2 = databaseConnection2.createStatement();

				ResultSet resultSet = neuesStatementCon2.executeQuery("SELECT Preis FROM Artikel WHERE Artikelnummer ='A1';");
				resultSet.next();
				int preis = resultSet.getInt(1);
				System.out.println("2: Preis = " + preis);
				Thread.sleep(100);
				neuesStatementCon2.executeUpdate("UPDATE Artikel set Preis=" + (preis + 100) + " where Artikelnummer='A1';");

				databaseConnection2.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				Connection databaseConnection2 = DriverManager.getConnection(url, user, pw);
				databaseConnection2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				databaseConnection2.setAutoCommit(false);
				Statement neuesStatementCon2 = databaseConnection2.createStatement();

				Thread.sleep(4000);
				ResultSet resultSet = neuesStatementCon2.executeQuery("SELECT Preis FROM Artikel WHERE Artikelnummer ='A1';");
				resultSet.next();
				int preis = resultSet.getInt(1);
				System.out.println("3: Preis = " + preis);

				databaseConnection2.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

}

package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Connect {
	private static Connect instance;
	private String URL;
	private String USER;
	private String PASSWORD;
	private String DRIVER;

	private Connect() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("db.xml");
			if (is == null) {
				throw new RuntimeException("Khong tim thay file db.xml");
			}

			Document doc = (Document) DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			doc.getDocumentElement().normalize();
			DRIVER = doc.getElementsByTagName("DRIVER").item(0).getTextContent();
			URL = doc.getElementsByTagName("URL").item(0).getTextContent();
			USER = doc.getElementsByTagName("USER").item(0).getTextContent();
			PASSWORD = doc.getElementsByTagName("PASSWORD").item(0).getTextContent();

			Class.forName(DRIVER);
		} catch (Exception e) {
			throw new RuntimeException("Loi load db config: " + e.getMessage());
		}
	}

	public static Connect getInstance() {
		if (instance == null) {
			instance = new Connect();
		}
		return instance;
	}

	public Connection getConnect() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}

package com.example.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		String url = "jdbc:sqlite:/Users/1mmauricio/blog.db";

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				System.out.println("Conexão com SQLite estabelecida!");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		SpringApplication.run(BlogApplication.class, args);
	}
}

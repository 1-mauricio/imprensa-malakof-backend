package com.example.blog;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BlogApplicationTests {

	private static String jwtToken;

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8080";

		given().contentType("application/json")
				.body("{\"username\":\"testuser\", \"password\":\"password123\"}")
				.post("/api/auth/register");

		Response response = given().contentType("application/json")
				.body("{\"username\":\"testuser\", \"password\":\"password123\"}")
				.post("/api/auth/login");

		jwtToken = response.jsonPath().getString("token");
	}

	@Test
	public void testCreatePost() {
		given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType("application/json")
				.body("{\"titulo\":\"Post Title\", \"conteudo\":\"Post Content\"}")
				.when()
				.post("/api/posts")
				.then()
				.statusCode(201)
				.body("titulo", equalTo("Post Title"));
	}

	@Test
	public void testGetAllPosts() {
		given()
				.header("Authorization", "Bearer " + jwtToken)
				.when()
				.get("/api/posts")
				.then()
				.statusCode(200)
				.body("$", notNullValue());
	}

	@Test
	public void testGetPostById() {
		// Criar um post para testar a busca
		Response response = given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType("application/json")
				.body("{\"titulo\":\"Test Post\", \"conteudo\":\"Content\"}")
				.post("/api/posts");

		Long postId = response.jsonPath().getLong("id");

		// Buscar o post pelo ID
		given()
				.header("Authorization", "Bearer " + jwtToken)
				.when()
				.get("/api/posts/" + postId)
				.then()
				.statusCode(200)
				.body("titulo", equalTo("Test Post"));
	}

	@Test
	public void testUpdatePost() {
		// Criar um post para teste
		Response response = given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType("application/json")
				.body("{\"titulo\":\"Old Title\", \"conteudo\":\"Old Content\"}")
				.post("/api/posts");

		Long postId = response.jsonPath().getLong("id");

		// Atualizar o post
		given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType("application/json")
				.body("{\"titulo\":\"New Title\"}")
				.when()
				.put("/api/posts/" + postId)
				.then()
				.statusCode(200)
				.body("titulo", equalTo("New Title"));
	}

	@Test
	public void testDeletePost() {
		// Criar um post para teste
		Response response = given()
				.header("Authorization", "Bearer " + jwtToken)
				.contentType("application/json")
				.body("{\"titulo\":\"To Delete\", \"conteudo\":\"Content\"}")
				.post("/api/posts");

		Long postId = response.jsonPath().getLong("id");

		// Deletar o post
		given()
				.header("Authorization", "Bearer " + jwtToken)
				.when()
				.delete("/api/posts/" + postId)
				.then()
				.statusCode(204);
	}


}

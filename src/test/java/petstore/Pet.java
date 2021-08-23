package petstore;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

public class Pet {

    String uri = "https://petstore.swagger.io/v2/pet";

    public String lerJson(String caminhoJson) throws IOException {

        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    @Test(priority = 1)
    public void incluirPet() throws IOException {

        String jsonBody = lerJson("src/test/resources/pets/pet1.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Tornado"))
                .body("status", is("available"))
                .body("category.name", is("horse"))
//                .body("tags.name[0]", is("sta"))
                .body("tags.name", contains("sta"))
        ;
    }

    @Test(priority = 2)
    public void consultarPet() {

        int petId = 1992102255;

        String categoryName =
        given()
                .contentType(ContentType.JSON)
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Tornado"))
                .body("category.name", is("horse"))
                .body("status", is("available"))
        .extract()
                .path("category.name")
        ;

        System.out.println("O categoryName é " + categoryName);
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {

        String jsonBody = lerJson("src/test/resources/pets/pet2.json");

        given()
                .contentType(ContentType.JSON)
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Tornado"))
                .body("status", is("sold"))
        ;
    }

    @Test(priority = 4)
    public void excluirPet() {

        String petId = "1992102255";

        given()
                .contentType(ContentType.JSON)
                .log().all()
        .when()
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }
}

package userApi;

import org.junit.jupiter.api.*;
import utils.ConfigLoader;

import java.util.List;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    private static final String baseUrl = new ConfigLoader().getProperty("user.url");
    private static final String usersListPath = "/api/users?page=2";
    private static final String userRegistrationPath = "/api/register";
    private static final String usersListResourcePath = "/api/unknown";
    private static final String usersDeleteResourcePath = "/api/users/2";

    @Test
    @Order(1)
    public void getUsersListAndVerifyIdWithAvatarId() {
        List<UserData> usersList = given().contentType("application/json")
                .when()
                .get(baseUrl + usersListPath)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        usersList.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assertions.assertTrue(usersList.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    @Order(2)
    public void getUsersListAndVerifyIdWithAvatarIdViaSpecification() {
        Specifications.runSpecification(Specifications.requestSpecification(baseUrl),
                Specifications.responseSpecification(200));

        List<UserData> usersList = given()
                .when()
                .get(usersListPath)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        List<String> avatarsList = usersList.stream().map(UserData::getAvatar).toList();
        List<String> idsList = usersList.stream().map(x -> x.getId().toString()).toList();

        for (int i = 0; i < avatarsList.size(); i++) {
            Assertions.assertTrue(avatarsList.get(i).contains(idsList.get(i)));
        }
    }

    @Test
    @Order(3)
    public void postRegistration() {
        Specifications.runSpecification(Specifications.requestSpecification(baseUrl),
                Specifications.responseSpecification(200));
        RegistrationRequestData registrationRequestData = new RegistrationRequestData("eve.holt@reqres.in", "pistol");

        RegistrationResponseData registrationResponseData = given()
                .body(registrationRequestData)
                .when()
                .post(userRegistrationPath)
                .then().log().all()
                .extract().body().as(RegistrationResponseData.class);
        Assertions.assertEquals(4, registrationResponseData.getId());
        Assertions.assertEquals("QpwL5tke4Pnpja7X4", registrationResponseData.getToken());
    }

    @Test
    @Order(4)
    public void postRegistrationFailed() {
        Specifications.runSpecification(Specifications.requestSpecification(baseUrl),
                Specifications.responseSpecification(400));

        RegistrationRequestData registrationRequestData = new RegistrationRequestData("sydney@fife", "");
        RegistrationResponseFailedData registrationResponseFailedData = given()
                .body(registrationRequestData)
                .when()
                .post(userRegistrationPath)
                .then().log().all()
                .extract().body().as(RegistrationResponseFailedData.class);
        Assertions.assertEquals("Missing password", registrationResponseFailedData.getError());
    }

    @Test
    @Order(5)
    public void getListResourcesSortedByYears() {
        Specifications.runSpecification(Specifications.requestSpecification(baseUrl),
                Specifications.responseSpecification(200));
        List<YearsListData> yearsList = given()
                .when()
                .get(usersListResourcePath)
                .then().log().all()
                .extract().body().jsonPath().getList("data", YearsListData.class);
        List<Integer> years = yearsList.stream().map(YearsListData::getYear).toList();
        List<Integer> sortedYears = yearsList.stream().map(YearsListData::getYear).sorted().toList();
        Assertions.assertEquals(sortedYears, years);
    }

    @Test
    @Order(6)
    public void deleteUser() {
        Specifications.runSpecification(Specifications.requestSpecification(baseUrl),
                Specifications.responseSpecification(204));
        given()
                .when()
                .delete(usersDeleteResourcePath)
                .then().log().all()
                .statusCode(204);
    }
}

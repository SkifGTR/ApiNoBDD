package bankApi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.ConfigLoader;

import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurrencyRatesFilteringTest {
    public static final int UAH = 980;

    @Test
    @Order(1)
    public void checkMonobank() {
        List<CurrencyData> uahCurrency = currencyData().stream().filter(x -> x.getCurrencyCodeB() == UAH).toList();
        Assertions.assertTrue(uahCurrency.stream().allMatch(x -> x.getCurrencyCodeB() == UAH));
    }

    @Test
    @Order(2)
    public void sortDescendingRateCross() {
        List<CurrencyData> descendingData = currencyData().stream().filter(x -> x.getCurrencyCodeB() == UAH)
                .filter(x -> x.getRateCross() > 0.0 & x.getRateCross() != 4.0E-4)
                .sorted(Comparator.comparing(CurrencyData::getRateCross).reversed()).toList();
        List<CurrencyData> highestCrossRates = descendingData.stream().limit(5).toList();
        Assertions.assertEquals(highestCrossRates.get(0).getCurrencyCodeA(), 414);
    }

    @Test
    @Order(3)
    public void sortAscendingRateCross() {
        List<CurrencyData> ascendingData = currencyData().stream()
                .filter(x -> x.getCurrencyCodeB() == UAH & x.getRateCross() > 0.0)
                .sorted(new CurrencyComparatorAscending())
                .limit(8).toList();
        Assertions.assertEquals(ascendingData.get(0).getCurrencyCodeA(), 422);
    }

    public List<CurrencyData> currencyData() {
        Response response = given().
                contentType(ContentType.JSON)
                .when()
                .get(new ConfigLoader().getProperty("bank.url"));

        int statusCode = response.getStatusCode();

        if (statusCode == 429) {
            System.out.println("Too many requests over there :(");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(new ConfigLoader().getProperty("bank.url"));
        }

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("API returns non-200 status: " + response.getStatusCode());
        }

        return response
                .then()
                .extract()
                .jsonPath().getList("$", CurrencyData.class);
    }
}

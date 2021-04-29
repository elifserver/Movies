package com.framework.steps;

import com.framework.pojos.RatingBody;
import com.framework.utils.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class MovieStepsAllTogether extends Base {

    static RequestSpecification res;
    Response response;
    JSONObject json;


    @Given("{string} Payload is created with APIKEY")
    public void payloadIsCreatedWithAPIKEY(String apiSubURI) throws IOException {
        res = given().spec(RequestSpecificationWithoutSeesionId(apiSubURI));
    }

    @When("User calls {string} with {string} request")
    public void callAPI(String apiResource, String method) {
        // response = commonStepFunctions.callAPI(res,apiResource,method);
        ApiResourceEnum enumValue = ApiResourceEnum.valueOf(apiResource);
        switch (method) {
            case "POST":
                response = res.when().post(enumValue.getResource());
                break;
            case "DELETE":
                response = res.when().delete(enumValue.getResource());
                break;
            case "GET":
                response = res.when().get(enumValue.getResource());
                break;
            default:
                break;
        }
    }

    @Then("The API call response status should be {int}")
    public void theAPICallResponseStatusShouldBe(int expectedResponseCode) {
        Assert.assertEquals(expectedResponseCode, response.getStatusCode());
    }

    @And("{string} should be {string}")
    public void shouldBe(String key, String value) {
        Assert.assertEquals(getResponseObjectKeyValue(response, key), value);
    }

    /**
     * Gets the expected Movie Name from strings.xml file.
     * I assumed xml file - latestMovie_title tag can be fed by another system with the latest movie.
     * For now as the values are static, step will FAIL.
     */
    @And("Movie should be the expected one")
    public void movieShouldBeTheExpectedOne() {
        String expectedMovieName = strings.get("latestMovie_title");
        Assert.assertEquals(expectedMovieName, getResponseObjectKeyValue(response, "title"));
    }

    /**
     * Gets the expected Movie Count from strings.xml file.
     * I assumed xml file - nowPlaying-count tag can be fed by another system with the actual count.
     * For now as the values are static, step will FAIL.
     */
    @And("Movie count should be the expected")
    public void movieCountShouldBeTheExpected() {
        String expectedMovieCount = strings.get("nowPlaying-count");
        Assert.assertEquals(expectedMovieCount, getResponseObjectKeyValue(response, "total_results"));
    }

    private boolean isMovieFound(String expectedMovieTitle) {
        int totalPagesCount = Integer.parseInt(getResponseObjectKeyValue(response, "total_pages"));
        JSONObject obj = getResponseAsJSONObject(response);
        JSONArray arr = obj.getJSONArray("results");
        for (int i = 0; i < totalPagesCount; i++) {
            for (int j = 0; j < arr.length(); j++) {
                if (arr.getJSONObject(j).get("title").toString().equalsIgnoreCase(expectedMovieTitle)) {
                    return true;
                }
            }
        }
        return false;
    }

    @And("Popular List should have {string}")
    public void isMovieOnTheList(String expectedMovieTitle) {
        Assert.assertTrue("Movie is not on the popular list. Or the title is wrong", isMovieFound(expectedMovieTitle));
    }

    @Given("SessionID is added the payload")
    public void sessionidIsAddedThePayload() throws IOException {

        String subURIForToken = getGlobalProperties("basePathForRequestToken");
        payloadIsCreatedWithAPIKEY(subURIForToken);

        /*
        It seems like it works without token for guest session. For the upcoming days, I keep it here
        callAPI("GetRequestTokenAPI", "GET");
        String token = getResponseObjectKeyValue(response, "request_token"); */

        callAPI("CreateGuestSessionAPI", "GET");
        String sessionID = getResponseObjectKeyValue(response, "guest_session_id");
        String movieUrl = new String();
        movieUrl = movieUrl.concat(getGlobalProperties("basePathForMovie"));
        movieUrl = movieUrl.concat(getGlobalProperties("movie_id"));
        setGlobalProperties("session_id", sessionID);

        res = given().spec(RequestSpecificationWithSeesionId(movieUrl));
    }

    @And("Movie with {string}  {string} is selected")
    public void movieWithIsSelected(String key, String value) throws IOException {
        setGlobalProperties("movie", value);
    }

    @And("User deletes movie rating")
    public void userDeletesMovieRating() {
        callAPI("GetRatingAPI", "DELETE");
    }

    @When("User rates movie {string}")
    public void userRatesMovie(String rate) throws IOException {

        RatingBody ratingBodyObj = new RatingBody();
        ratingBodyObj.setKey("value");
        ratingBodyObj.setValue(rate);

        res = res.body(ratingBodyObj);
        callAPI("GetRatingAPI", "POST");
    }

    @Then("User should get {string}")
    public void userShouldGet(String expectedErrorMessage) {
        String statusMessage = getResponseObjectKeyValue(response, "status_message");
        Assert.assertEquals(expectedErrorMessage, statusMessage);
    }
}

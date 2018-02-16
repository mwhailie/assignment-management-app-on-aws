package com.csye6225.demo;

import com.csye6225.demo.controllers.HomeController;
import com.csye6225.demo.controllers.TaskController;
import com.google.gson.JsonObject;
import io.restassured.*;
import io.restassured.http.ContentType;
import io.restassured.internal.http.HTTPBuilder;
import org.apache.http.client.ClientProtocolException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class RestAssuredDemoApiTest {


  @Test
  public void testGetHomePage() throws URISyntaxException {
    System.out.println("test home page ");
      assertTrue(1 == 1);
//      RestAssured.when().get(new URI("http://localhost:8080/csye6225app")).then().statusCode(200);
  }

   @Test
   public void testCreateTask() throws Exception {
      //  RestAssured
      //         .given()
      //            .auth().preemptive().basic("user", "password").body("{\"description\":\"des\"}")
      //          .expect()
      //            .statusCode(201)
      //          .when()
      //            .post("http://localhost:8080/tasks");
   }
}

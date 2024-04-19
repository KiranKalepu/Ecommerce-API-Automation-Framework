package Automation.ECommerceAPITesting;

import org.testng.Assert;
import org.testng.annotations.Test;

import Automation.ECommerceAPITesting.pojoClasses.CreateProductResponseDetails;
import Automation.ECommerceAPITesting.pojoClasses.LoginResponseDetails;
import Automation.ECommerceAPITesting.pojoClasses.Orders;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;

import java.io.File;

public class ECommerceAppAPITest extends BaseTest
{

    @Test
    public void getAuthorizedTokenTest()
    {
        RequestSpecification loginRequest = given().spec(loginRequestBuilder()).body(setLoginDetails());
        LoginResponseDetails reponseDetails = loginRequest.when().post("/api/ecom/auth/login").then().assertThat().statusCode(200).extract().response().as(LoginResponseDetails.class);
        token = reponseDetails.getToken();
        userId = reponseDetails.getUserId();
        Assert.assertTrue(reponseDetails.getMessage().equals("Login Successfully"));
    }

    @Test(dependsOnMethods = {"getAuthorizedTokenTest"})
    public void createProductTest()
    {
        RequestSpecification createProductRequest = given().spec(authorizedTokenBuilder()).formParam("productName", "test productezo")
        .formParam("productAddedBy", userId)
        .formParam("productCategory", "Electronics")
        .formParam("productSubCategory", "laptops")
        .formParam("productPrice", "76000")
        .formParam("productDescription", "Automation Testing tool")
        .formParam("productFor", "women").multiPart("productImage", new File("./Images\\laptop2.jpg"));
        CreateProductResponseDetails createProductResponseDetails = createProductRequest.when().post("api/ecom/product/add-product").then().extract().as(CreateProductResponseDetails.class);
        productId = createProductResponseDetails.getProductId();
        Assert.assertTrue(createProductResponseDetails.getMessage().equals("Product Added Successfully"));
    }

    @Test(dependsOnMethods = {"createProductTest"})
    public void createOrderTest()
    {
        RequestSpecification createOrderRequest = given().spec(authorizedTokenCreateOrderBuilder()).body(setCreateOrderDetails());
        Orders createOrderResponse = createOrderRequest.when().post("api/ecom/order/create-order").then().extract().response().as(Orders.class);
        Assert.assertTrue(createOrderResponse.getMessage().equals("Order Placed Successfully"));
        orderId = createOrderResponse.getOrders().get(0);
    }
    
    @Test(dependsOnMethods = {"createOrderTest"})
    public void getOrderDetailsTest()
    {
    	RequestSpecification getOrderDetailsRequest = given().spec(authorizedTokenBuilder()).queryParam("id", orderId);
    	String orderDetails = getOrderDetailsRequest.when().get("api/ecom/order/get-orders-details").then().assertThat().statusCode(200).extract().response().asString();	
    	JsonPath js = new JsonPath(orderDetails);
    	Assert.assertEquals(js.getString("message"), "Orders fetched for customer Successfully");
    }
    
    @Test(dependsOnMethods = {"createProductTest"})
    public void deleteCreatedProduct()
    {
    	RequestSpecification deleteProductRequest = given().spec(authorizedTokenBuilder()).pathParam("productId", productId);
    	String deleteResponse = deleteProductRequest.when().delete("api/ecom/product/delete-product/{productId}").then().assertThat().statusCode(200).extract().response().asString();
    	JsonPath js = new JsonPath(deleteResponse);
    	Assert.assertEquals(js.getString("message"), "Product Deleted Successfully");
    }
    
    @Test(dependsOnMethods = {"createOrderTest"})
    public void deleteCreatedOrder()
    {
    	RequestSpecification deleteOrderRequest = given().spec(authorizedTokenBuilder()).pathParam("orderId", orderId);
    	String deleteResponse = deleteOrderRequest.when().delete("api/ecom/order/delete-order/{orderId}").then().log().all().assertThat().statusCode(200).extract().response().asString();
    	JsonPath js = new JsonPath(deleteResponse);
    	Assert.assertEquals(js.getString("message"), "Orders Deleted Successfully");
    }
}

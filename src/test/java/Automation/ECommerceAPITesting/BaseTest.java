package Automation.ECommerceAPITesting;

import java.util.ArrayList;
import java.util.List;

import Automation.ECommerceAPITesting.pojoClasses.CreateOrder;
import Automation.ECommerceAPITesting.pojoClasses.LoginDetails;
import Automation.ECommerceAPITesting.pojoClasses.OrderDetails;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseTest {
    String token = "";
    String userId = "";
    String productId = "";
    String orderId = "";

    public LoginDetails setLoginDetails()
    {
    	LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUserEmail("tezotester@gmail.com");
        loginDetails.setUserPassword("TezoTester@1");
        return loginDetails;
    }

    public CreateOrder setCreateOrderDetails()
    {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(productId);
        List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        orderDetailsList.add(orderDetails);
        CreateOrder orders = new CreateOrder();
        orders.setOrders(orderDetailsList);
        return orders;
    }

    public RequestSpecification loginRequestBuilder()
    {
        RequestSpecification loginRequestBuilder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
        return loginRequestBuilder;
    }

    public RequestSpecification authorizedTokenBuilder()
    {
        RequestSpecification authorizedTokenBuilder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", token).build();
        return authorizedTokenBuilder;
    }
    
    public RequestSpecification authorizedTokenCreateOrderBuilder()
    {
        RequestSpecification authorizedTokenBuilder = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).addHeader("Authorization", token).build();
        return authorizedTokenBuilder;
    }
}

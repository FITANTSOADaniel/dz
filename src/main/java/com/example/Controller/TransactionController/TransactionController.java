package com.example.Controller.TransactionController;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path ("/test")
public class TransactionController {
    @GET
    public String hello() {
        return "Hello Test";
    }
}

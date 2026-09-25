package com.example.Controller;

import java.io.IOException;
import java.util.List;

import com.example.models.Entity.User;
import com.example.services.UserService;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/users")
public class UserList extends HttpServlet{

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userService.getAll();

        request.setAttribute("userList", users);
        request.getRequestDispatcher("/views/usersList.jsp").forward(request, response);
    }
}

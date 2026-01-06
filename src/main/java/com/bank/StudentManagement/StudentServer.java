package com.bank.StudentManagement;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.ArrayList;

public class StudentServer {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4567); // server runs on localhost:4567

        // ===== Enable CORS =====
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        // ===== Routes =====

        // Add student
        post("/addStudent", (req, res) -> {
            Student s = gson.fromJson(req.body(), Student.class);
            students.add(s);
            res.type("application/json");
            return gson.toJson("Student added successfully");
        });

        // Get all students
        get("/students", (req, res) -> {
            res.type("application/json");
            return gson.toJson(students);
        });

        // Search student by ID
        get("/students/:id", (req, res) -> {
            String id = req.params(":id");
            for (Student s : students) {
                if (s.getId().equals(id)) {
                    res.type("application/json");
                    return gson.toJson(s);
                }
            }
            res.status(404);
            return gson.toJson("Student not found");
        });

        // Delete student
        delete("/students/:id", (req, res) -> {
            String id = req.params(":id");
            boolean removed = students.removeIf(s -> s.getId().equals(id));
            res.type("application/json");
            if (removed) return gson.toJson("Deleted successfully");
            else {
                res.status(404);
                return gson.toJson("Student not found");
            }
        });

        // Update student
        put("/students/:id", (req, res) -> {
            String id = req.params(":id");
            Student updated = gson.fromJson(req.body(), Student.class);
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(id)) {
                    students.set(i, updated);
                    res.type("application/json");
                    return gson.toJson("Updated successfully");
                }
            }
            res.status(404);
            return gson.toJson("Student not found");
        });

        System.out.println("Student server running at http://localhost:4567");
    }
}

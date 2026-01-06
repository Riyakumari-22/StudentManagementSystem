package com.bank.StudentManagement;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Class to represent a student
class Student implements Serializable {
    private String id;
    private String name;
    private String course;
    private double marks;

    public Student(String id, String name, String course, double marks) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.marks = marks;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public String toString() {
        return String.format("%-10s %-15s %-10s %-5.2f", id, name, course, marks);
    }
}

public class App {
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.dat";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadStudents();
        int choice;

        do {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 : addStudent();
                break;
                case 2 : viewStudents();
                break;
                case 3 : searchStudent();
                break;
                case 4 : updateStudent();
                break;
                case 5 : deleteStudent();
                break;
                case 6 : saveStudents();
                break;
                default :System.out.println("Invalid choice!");
            }
        } while (choice != 6);
    }

    private static void addStudent() {
        System.out.print("Enter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();
        System.out.print("Enter Marks: ");
        double marks = sc.nextDouble();
        sc.nextLine(); // consume newline

        students.add(new Student(id, name, course, marks));
        System.out.println("Student added successfully!");
    }

    private static void viewStudents() {
        System.out.println("\nID         Name            Course     Marks");
        System.out.println("-------------------------------------------");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    private static void searchStudent() {
        System.out.print("Enter Student ID to search: ");
        String id = sc.nextLine();
        for (Student s : students) {
            if (s.getId().equals(id)) {
                System.out.println("Found: " + s);
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = sc.nextLine();
        for (Student s : students) {
            if (s.getId().equals(id)) {
                System.out.print("Enter new Name: ");
                s.setName(sc.nextLine());
                System.out.print("Enter new Course: ");
                s.setCourse(sc.nextLine());
                System.out.print("Enter new Marks: ");
                s.setMarks(sc.nextDouble());
                sc.nextLine(); // consume newline
                System.out.println("Student updated!");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        String id = sc.nextLine();
        for (Student s : students) {
            if (s.getId().equals(id)) {
                students.remove(s);
                System.out.println("Student deleted!");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void saveStudents() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(students);
            System.out.println("Data saved. Exiting...");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadStudents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) in.readObject();
        } catch (FileNotFoundException e) {
            // File not found on first run - ignore
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
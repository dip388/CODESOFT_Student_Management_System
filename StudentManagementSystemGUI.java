import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private char grade;

    public Student(String name, int rollNumber, char grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public char getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

class StudentManagementSystem {
    private ArrayList<Student> students;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> getAllStudents() {
        return students;
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class StudentManagementSystemGUI extends JFrame {
    private StudentManagementSystem studentManagementSystem;
    private JTextField nameField, rollNumberField, gradeField;
    private JTextArea outputArea;

    public StudentManagementSystemGUI() {
        this.studentManagementSystem = new StudentManagementSystem();

        setTitle("Student Management System");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        add(panel);

        JLabel nameLabel = new JLabel("Name:");
        JLabel rollNumberLabel = new JLabel("Roll Number:");
        JLabel gradeLabel = new JLabel("Grade:");

        nameField = new JTextField();
        rollNumberField = new JTextField();
        gradeField = new JTextField();

        JButton addStudentButton = new JButton("Add Student");
        JButton searchStudentButton = new JButton("Search Student");
        JButton displayAllStudentsButton = new JButton("Display All Students");

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(rollNumberLabel);
        panel.add(rollNumberField);
        panel.add(gradeLabel);
        panel.add(gradeField);
        panel.add(addStudentButton);
        panel.add(searchStudentButton);
        panel.add(displayAllStudentsButton);
        panel.add(outputArea);

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        searchStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        displayAllStudentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllStudents();
            }
        });
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            char grade = gradeField.getText().charAt(0);

            Student student = new Student(name, rollNumber, grade);
            studentManagementSystem.addStudent(student);

            displayMessage("Student added successfully.");
        } catch (NumberFormatException | StringIndexOutOfBoundsException ex) {
            displayMessage("Invalid input. Please enter valid information.");
        }
    }

    private void searchStudent() {
        try {
            int rollNumber = Integer.parseInt(rollNumberField.getText());
            Student student = studentManagementSystem.searchStudent(rollNumber);

            if (student != null) {
                displayMessage("Student found: " + student.toString());
            } else {
                displayMessage("Student not found.");
            }
        } catch (NumberFormatException ex) {
            displayMessage("Invalid input. Please enter a valid roll number.");
        }
    }

    private void displayAllStudents() {
        ArrayList<Student> students = studentManagementSystem.getAllStudents();

        if (!students.isEmpty()) {
            StringBuilder message = new StringBuilder("All Students:\n");
            for (Student student : students) {
                message.append(student.toString()).append("\n");
            }
            displayMessage(message.toString());
        } else {
            displayMessage("No students available.");
        }
    }

    private void displayMessage(String message) {
        outputArea.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StudentManagementSystemGUI().setVisible(true);
            }
        });
    }
}


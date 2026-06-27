import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeTracker {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> studentNames = new ArrayList<>();
        ArrayList<Double> studentGrades = new ArrayList<>();
        
        System.out.println("=== CodeAlpha Student Grade Tracker ===");
        System.out.println("Enter student details. Type 'done' when you are finished.\n");
        
        while (true) {
            System.out.print("Enter student name (or 'done' to finish): ");
            String name = scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
                continue;
            }
            
            double grade = -1;
            while (true) {
                System.out.print("Enter grade for " + name + " (0 - 100): ");
                try {
                    grade = Double.parseDouble(scanner.nextLine());
                    if (grade >= 0 && grade <= 100) {
                        break;
                    } else {
                        System.out.println("Invalid input. Grade must be between 0 and 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid numerical grade.");
                }
            }
            
            studentNames.add(name);
            studentGrades.add(grade);
            System.out.println("Added: " + name + " with a grade of " + grade + "\n");
        }
        
        // Check if any data was entered
        if (studentNames.isEmpty()) {
            System.out.println("\nNo student data was entered. Exiting program.");
            scanner.close();
            return;
        }
        
        // Calculations
        double total = 0;
        double highest = studentGrades.get(0);
        double lowest = studentGrades.get(0);
        String highestStudent = studentNames.get(0);
        String lowestStudent = studentNames.get(0);
        
        for (int i = 0; i < studentGrades.size(); i++) {
            double currentGrade = studentGrades.get(i);
            total += currentGrade;
            
            if (currentGrade > highest) {
                highest = currentGrade;
                highestStudent = studentNames.get(i);
            }
            
            if (currentGrade < lowest) {
                lowest = currentGrade;
                lowestStudent = studentNames.get(i);
            }
        }
        
        double average = total / studentGrades.size();
        
        // Summary Report
        System.out.println("\n=================================");
        System.out.println("         SUMMARY REPORT          ");
        System.out.println("=================================");
        System.out.printf("Total Students Registered: %d\n", studentNames.size());
        System.out.printf("Class Average Grade:       %.2f\n", average);
        System.out.printf("Highest Grade:             %.2f (%s)\n", highest, highestStudent);
        System.out.printf("Lowest Grade:              %.2f (%s)\n", lowest, lowestStudent);
        System.out.println("=================================");
        
        System.out.println("\nAll Student Records:");
        for (int i = 0; i < studentNames.size(); i++) {
            System.out.printf("- %s: %.2f\n", studentNames.get(i), studentGrades.get(i));
        }
        System.out.println("=================================");
        
        scanner.close();
    }
}
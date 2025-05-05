package cw06;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Student {
    String surname;
    int index;

    public Student(String surname, int index) {
        this.surname = surname;
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return index == student.index && Objects.equals(surname, student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, index);
    }

    @Override
    public String toString() {
        return "Student{" +
                "surname='" + surname + '\'' +
                ", index=" + index +
                '}';
    }
}

public class HashMapExample {
    public static void main(String[] args) {
        Map<Student, String> studentMap = new HashMap<>();

        Student student1 = new Student("Kowalski", 12345);
        Student student2 = new Student("Nowak", 67890);

        studentMap.put(student1, "Mathematics");
        studentMap.put(student2, "Physics");

        // Wyszukiwanie studenta w mapie
        Student searchStudent = new Student("Kowalski", 12345);
        String course = studentMap.get(searchStudent);
        if (course != null) {
            System.out.println(searchStudent + " enrolled in: " + course);
        } else {
            System.out.println("Student not found.");
        }
    }
}

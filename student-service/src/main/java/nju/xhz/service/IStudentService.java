package nju.xhz.service;

import nju.xhz.model.Student;

import java.util.List;

public interface IStudentService {
    List<Student> getAllStudents();
    void saveStudent(Student s);
    void deleteStudent(String id);
    void updateScore(String id, float regular, float project, float fin, float total);
    boolean findById(String id);
}

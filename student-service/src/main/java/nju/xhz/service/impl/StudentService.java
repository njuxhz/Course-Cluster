package nju.xhz.service.impl;

import nju.xhz.model.Student;
import nju.xhz.repository.StudentJpaRepository;
import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService implements IStudentService{
    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentJpaRepository.findAll();
    }

    @Override
    public void saveStudent(Student s) {
        studentJpaRepository.save(s);
    }

    @Override
    public void deleteStudent(String id) {
        studentJpaRepository.delete(id);
    }

    @Transactional
    @Override
    public void updateScore(String id, float regular, float project, float fin, float total) {
        Student s = studentJpaRepository.findOne(id);
        s.setRegular(regular);
        s.setProject(project);
        s.setFin(fin);
        s.setTotal(total);
        studentJpaRepository.save(s);
    }

    @Override
    public boolean findById(String id) {
        Student s = studentJpaRepository.findOne(id);
        if(s != null) return false;
        return true;
    }
}

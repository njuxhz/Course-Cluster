package nju.xhz.controller;

import nju.xhz.model.Student;
import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {
    @Autowired
    private IStudentService studentService;

    @GetMapping("/student")
    @ResponseBody
    public List<Student> get() {
        List<Student> studentList = studentService.getAllStudents();
        return studentList;
    }

    @PostMapping("/student")
    @ResponseBody
    public Student add(@RequestParam(value = "id", defaultValue = "0") String id,
                      @RequestParam(value = "name", defaultValue = "xhz") String name,
                      @RequestParam(value = "major", defaultValue = "computer") String major) {
        boolean isOK = studentService.findById(id);
        if(isOK) {
            Student s = new Student(id, name, major);
            System.out.println(s.getName());
            studentService.saveStudent(s);
            return s;
        }
        return null;
    }

    @DeleteMapping("/student/{id}")
    @ResponseBody
    public Student delete(@PathVariable String id) {
        studentService.deleteStudent(id);
        return null;
    }

    @PutMapping("/student/{oldId}")
    @ResponseBody
    public Student update(@PathVariable String oldId,
                         @RequestParam(value = "id", defaultValue = "0") String id,
                         @RequestParam(value = "name", defaultValue = "xhz") String name,
                         @RequestParam(value = "major", defaultValue = "computer") String major,
                         @RequestParam(value = "regular", defaultValue = "0") String regular,
                         @RequestParam(value = "project", defaultValue = "0") String project,
                         @RequestParam(value = "final", defaultValue = "0") String fin,
                         @RequestParam(value = "total", defaultValue = "0") String total) {
        studentService.deleteStudent(oldId);
        Student s = new Student(id, name, major, Float.parseFloat(regular), Float.parseFloat(project),
                Float.parseFloat(fin), Float.parseFloat(total));
        studentService.saveStudent(s);
        return s;
    }
}

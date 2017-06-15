package nju.xhz.controller;

import nju.xhz.model.Student;
import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ScoreController {
    @Autowired
    private IStudentService studentService;

    @PutMapping("/score/{id}")
    @ResponseBody
    public Student update(@PathVariable String id,
                          @RequestParam(value = "regular",defaultValue = "0") String regular,
                          @RequestParam(value = "project",defaultValue = "0") String project,
                          @RequestParam(value = "final",defaultValue = "0") String fin,
                          @RequestParam(value = "total",defaultValue = "0") String total) {
        return studentService.updateScore(id, Float.parseFloat(regular), Float.parseFloat(project),
                Float.parseFloat(fin), Float.parseFloat(total));
    }
}

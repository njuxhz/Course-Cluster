package nju.xhz.controller;

import nju.xhz.batch.excel.poi.PoiItemReader;
import nju.xhz.model.Student;
import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class FileController {
    @Autowired
    private IStudentService studentService;

    @PostMapping("/file")
    @ResponseBody
    public List<Student> upload(@RequestParam("file") MultipartFile file) {
        File f = null;
        try {
            f=File.createTempFile("tmp", null);
            file.transferTo(f);
            f.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] springConfig  = { "batch/job.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("uploadJob");

        FileSystemResource res = new FileSystemResource(f);
        PoiItemReader reader = (PoiItemReader) context.getBean("studentReader");
        reader.setResource(res);

        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Exit Status : " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Done");

        List<Student> studentList = studentService.getAllStudents();
        return studentList;
    }
}

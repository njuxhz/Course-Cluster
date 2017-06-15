package nju.xhz.controller;

import nju.xhz.model.Student;
import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/studentClient")
    @ResponseBody
    public List<Student> get() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("student-service");
        System.out.println("===" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
                + serviceInstance.getPort());
        Student[] studentLists = restTemplate.getForObject("http://student-service/student", Student[].class);

        List<Student> students = new ArrayList<>(0);
        for(Student s : studentLists) students.add(s);
        return students;
    }

    @PostMapping("/studentClient")
    @ResponseBody
    public int add(@RequestParam(value = "id", defaultValue = "0") String id,
                    @RequestParam(value = "name", defaultValue = "xhz") String name,
                    @RequestParam(value = "major", defaultValue = "computer") String major) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("student-service");
        System.out.println("===" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
                + serviceInstance.getPort());

        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("id", id);
        requestEntity.add("name", name);
        requestEntity.add("major", major);
        Student s = restTemplate.postForObject("http://student-service/student", requestEntity, Student.class);
        return 1;
    }

    @DeleteMapping("/studentClient/{id}")
    @ResponseBody
    public int delete(@PathVariable String id) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("student-service");
        System.out.println("===" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
                + serviceInstance.getPort());

        restTemplate.delete("http://student-service/student/" + id);
        return 1;
    }

    @PutMapping("/studentClient/{oldId}")
    @ResponseBody
    public int update(@PathVariable String oldId,
                      @RequestParam(value = "id", defaultValue = "0") String id,
                      @RequestParam(value = "name", defaultValue = "xhz") String name,
                      @RequestParam(value = "major", defaultValue = "computer") String major,
                      @RequestParam(value = "regular", defaultValue = "0") String regular,
                      @RequestParam(value = "project", defaultValue = "0") String project,
                      @RequestParam(value = "final", defaultValue = "0") String fin,
                      @RequestParam(value = "total", defaultValue = "0") String total) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("student-service");
        System.out.println("===" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
                + serviceInstance.getPort());

        restTemplate.put("http://student-service/student/" + oldId + "?" +
                "id=" + id + "&name=" + name + "&major=" + major + "&regular=" + regular +
                "&project=" + project + "&final=" + fin + "&total=" + total,null);
        return 1;
    }
}

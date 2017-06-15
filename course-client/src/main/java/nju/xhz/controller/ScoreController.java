package nju.xhz.controller;

import nju.xhz.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class ScoreController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @PutMapping("/scoreClient/{id}")
    @ResponseBody
    public int update(@PathVariable String id,
                      @RequestParam(value = "regular",defaultValue = "0") String regular,
                      @RequestParam(value = "project",defaultValue = "0") String project,
                      @RequestParam(value = "final",defaultValue = "0") String fin,
                      @RequestParam(value = "total",defaultValue = "0") String total) {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("score-service");
        System.out.println("===" + ":" + serviceInstance.getServiceId() + ":" + serviceInstance.getHost() + ":"
                + serviceInstance.getPort());

        restTemplate.put("http://score-service/score/" + id + "?" +
                "regular=" + regular + "&project=" + project + "&final=" + fin + "&total=" + total,null);
        return 1;
    }
}

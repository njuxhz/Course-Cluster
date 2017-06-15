package nju.xhz.batch;

import org.springframework.batch.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<String[], StudentPJ> {
    @Override
    public StudentPJ process(String[] strings) throws Exception {
        StudentPJ s = new StudentPJ();
        s.setId(strings[0]);
        s.setName(strings[1]);
        s.setMajor(strings[2]);
        return s;
    }
}
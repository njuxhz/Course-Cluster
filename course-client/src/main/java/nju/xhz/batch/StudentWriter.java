package nju.xhz.batch;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentWriter implements ItemWriter<StudentPJ> {

    private static final String GET_STUDENT = "select * from student where id = ?";
    private static final String INSERT_STUDENT =
            "insert into student (id,name,major, regular, project, final, total) values (?,?,?, 0, 0, 0, 0)";

    @Override
    public void write(List<? extends StudentPJ> list) throws Exception {
        String[] springConfig  = { "batch/database.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
        JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

        for(StudentPJ s : list) {
            List<StudentPJ> studentList = jdbcTemplate.query(GET_STUDENT, new Object[] {s.getId()}, new RowMapper<StudentPJ>() {
                @Override
                public StudentPJ mapRow(ResultSet resultSet, int rowNum ) throws SQLException {
                    StudentPJ s = new StudentPJ();
                    s.setId( resultSet.getString( 1 ) );
                    s.setName( resultSet.getString( 2 ) );
                    s.setMajor( resultSet.getString( 3 ) );
                    return s;
                }
            });
            if(studentList == null || studentList.size() == 0) {
                jdbcTemplate.update( INSERT_STUDENT, s.getId(), s.getName(), s.getMajor());
            }
        }
    }
}
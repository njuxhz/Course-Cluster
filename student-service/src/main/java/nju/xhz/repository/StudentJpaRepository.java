package nju.xhz.repository;

import nju.xhz.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentJpaRepository extends JpaRepository<Student, String> {
}

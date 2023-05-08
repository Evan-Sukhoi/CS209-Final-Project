package cse.java2.project.questions;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Evan
 */
public interface PersonRepository extends JpaRepository<Question, Long> {

}

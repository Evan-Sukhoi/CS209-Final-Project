package cse.java2.project.model;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Evan
 */
@Entity
public class Questions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int question_id;
  public Integer answer_count;
  public boolean is_answered;
  public LocalDateTime creation_date;
  public LocalDateTime accepted_date;
  public Integer accepted_answer_id;

}

package cse.java2.project.questions;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Evan
 */
@Entity
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;
  public int answerCount;
  public boolean isAnswered;
  public LocalDateTime creationDate;
  public LocalDateTime acceptedDate;
  public int acceptedAnswerId;

}

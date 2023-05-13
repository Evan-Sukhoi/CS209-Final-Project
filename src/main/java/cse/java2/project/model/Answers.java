package cse.java2.project.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Answers {

  @Id
  public Long answer_id;
  public Long question_id;
  public String body;
}

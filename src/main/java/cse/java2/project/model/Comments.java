package cse.java2.project.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Comments {

  @Id
  public Long comment_id;
  public Long question_id;
  public String body;

}

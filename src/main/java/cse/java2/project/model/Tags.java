package cse.java2.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tags {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public
  String name;
  public int score;
  public int view_count;
  public int count;
}

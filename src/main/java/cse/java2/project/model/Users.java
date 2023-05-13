package cse.java2.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int account_id;
  public String display_name;
  public int count;
}

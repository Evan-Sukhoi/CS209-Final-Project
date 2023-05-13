package cse.java2.project.model;

import javax.persistence.*;

@Entity
@Table(name = "hot_api")
public class HotApi {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public
  int id;
  public String name;
  public int count;
}

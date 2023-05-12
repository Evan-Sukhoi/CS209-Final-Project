package cse.java2.project.model;

import javax.persistence.*;

@Entity
@Table(name = "tags_java_related")
public class TagsJavaRelated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String name;
    public int count;

}

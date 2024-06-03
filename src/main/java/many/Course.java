package many;

import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @ManyToMany(mappedBy = "courses",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Student> students;
    
    public Course() {}
    public Course(String name) {
    	this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + "]";
    }
}

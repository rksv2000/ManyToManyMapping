package many;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/students")
public class StudentController {
     @Autowired
     private StudentRepo sr;
     
     @Autowired
     private CourseRepo cr;
     
     @PostMapping
     public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
         Set<Course> courses = student.getCourses();
         if (courses != null && !courses.isEmpty()) {
             for (Course course : courses) {
                 Optional<Course> existingCourse = cr.findById(course.getId());
                 if (existingCourse.isPresent()) {
                     student.getCourses().remove(course);
                     student.getCourses().add(existingCourse.get());
                 }
             }
                 }
 Student saveStudent = sr.save(student);
         return new ResponseEntity<>(saveStudent, HttpStatus.CREATED);
     }
     
     
     @GetMapping
     public ResponseEntity<List<Student>> getAllStudents() {
    	 List<Student> students = sr.findAll();
    	 return new ResponseEntity<>(students, HttpStatus.OK);
     }
     
     @GetMapping("/{id}")
     public ResponseEntity<Student> getStudentById(@PathVariable int id) {
    	 Optional<Student> student = sr.findById(id);
    	 return student.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
     }
     
     @PutMapping("/{id}")
     public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
    	 Optional<Student> existingStudent = sr.findById(id);
    	 if(existingStudent.isPresent()) {
    		 Student student = existingStudent.get();
    		 student.setName(updatedStudent.getName());
    		 student.setDob(updatedStudent.getDob());
    		 student.setClasss(updatedStudent.getClasss());
    		 student.setDivision(updatedStudent.getDivision());
    		 student.setGender(updatedStudent.getGender());  
    		 Set<Course> courses = updatedStudent.getCourses();
             if (courses != null && !courses.isEmpty()) {
                 student.getCourses().clear();
                 for (Course course : courses) {
                     Optional<Course> existingCourse = cr.findById(course.getId());
                     existingCourse.ifPresent(value -> student.getCourses().add(value));

                 }
             }
    		 Student saveStudent = sr.save(student);
        	 return new ResponseEntity<>(saveStudent, HttpStatus.OK);
    	 }
    	 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
     
     @DeleteMapping("/{id}")
     public ResponseEntity<Student> deleteStudent(@PathVariable int id) {
         Optional<Student> studentOptional = sr.findById(id);
         if (studentOptional.isPresent()) {
             Student student = studentOptional.get();
             
             if (student.getCourses() != null) {
                 student.getCourses().clear();
             }

             sr.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
         } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }
}

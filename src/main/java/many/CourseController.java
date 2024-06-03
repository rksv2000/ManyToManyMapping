package many;

import java.util.List;
import java.util.Optional;

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
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseRepo cr;
    
    @PostMapping
    public ResponseEntity<Course> saveCourse(@RequestBody Course course) {
    	Course saveCourse = cr.save(course);
    	return new ResponseEntity<>(saveCourse, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
    	List<Course> courses = cr.findAll();
    	return new ResponseEntity<>(courses, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
    	Optional<Course> course = cr.findById(id);
    	return course.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
    	Optional<Course> existingSource = cr.findById(id);
    	if(existingSource.isPresent()) {
    		Course course = existingSource.get();
    		course.setName(updatedCourse.getName());
    		Course saveCourse = cr.save(course);
        	return new ResponseEntity<>(saveCourse, HttpStatus.OK);
    	}
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        Optional<Course> course = cr.findById(id);

        if (course.isPresent()) {
            cr.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

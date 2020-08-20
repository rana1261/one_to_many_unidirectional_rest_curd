package com.demo.one_to_many.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import com.demo.one_to_many.model.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.one_to_many.exception.NotFoundException;
import com.demo.one_to_many.model.Student;
import com.demo.one_to_many.repository.StudentRepository;


@RestController
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    /* post a Student and also post more than one child (Laptop) in third braket -->
     "laptop":[{"laptopName":"Samsung Pro-50"},
               {"laptopName":"Mackbook Pro-50"}
              ]
    */
    @PostMapping(value = "students")
    public void saveStudent(@Valid @RequestBody Student student) {

        studentRepository.save(student);
    }


    /* we get the list of all student and it's child(Laptop) data*/
    @GetMapping(value = "students")
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }


    /* we get a student by id and it's child(Laptop) data*/
    @GetMapping("/students/{id}")
    public Student getStudentByID(@PathVariable Long id) {
        Optional<Student> optStudent = studentRepository.findById(id);
        if (optStudent.isPresent()) {
            return optStudent.get();
        } else {
            throw new NotFoundException("Student not found with id " + id);
        }
    }
		/*
	@GetMapping(value="students/{id}")
	public Student getStudentById(@PathVariable Long id) {
		return studentRepository.getOne(id);
	}
	*/




    /*Delete student by id and delete it's child (Laptop) also*/
    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return "Delete Successfully!";
                }).orElseThrow(() -> new NotFoundException("Student not found with id " + id));
    }
	/*@DeleteMapping(value="students/{id}")
	public void delete(@PathVariable Long id) {
		studentRepository.deleteById(id);
	}
	*/



    /*update student by id and not update it's child (Laptop)*/
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @Valid @RequestBody Student studentUpdated) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStudentName(studentUpdated.getStudentName());
                    student.setStudentMark(studentUpdated.getStudentMark());
                    //Laptop laptop = student.getLaptop();
                    //laptop.setLaptopName(studentUpdated.getLaptop().getLaptopName());
                    Set<Laptop> laptop = student.getLaptop();
                    laptop=studentUpdated.getLaptop();
                    student.setLaptop(laptop);
                    return studentRepository.save(student);
                }).orElseThrow(() -> new NotFoundException("Student not found with id " + id));
    }
    /*	@PutMapping(value="students/update")
	public void updateStudent(@RequestBody Student student) {
		Student su=studentRepository.getOne(student.getSRoll());
		su.setSName(student.getSName());
		su.setSMark(student.getSMark());
		su.setLaptop(student.getLaptop());
		studentRepository.save(su);
	}*/


}

package com.demo.one_to_many.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import com.demo.one_to_many.model.Laptop;
import com.demo.one_to_many.repository.LaptopRepository;
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
    @Autowired
    LaptopRepository laptopRepository;

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
    public Student justUpdateStudent(@PathVariable Long id,
                                 @Valid @RequestBody Student studentUpdated) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStudentName(studentUpdated.getStudentName());
                    student.setStudentMark(studentUpdated.getStudentMark());
                    return studentRepository.save(student);
                }).orElseThrow(() -> new NotFoundException("Student not found with id " + id));
    }



    /*   student id er under e laptop gulor akta laptop id dhore update korbe.
        akane kono student table er data update kora jabe na*/
    @PutMapping("students/{studentId}/laptops/{laptopId}")
    public Laptop updateLaptop(@PathVariable Long studentId, @PathVariable Long laptopId,
                               @Valid @RequestBody Laptop laptopUpdated) {

        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found!");
        }

        return laptopRepository.findById(laptopId)
                .map(laptop -> {
                    laptop.setLaptopName(laptopUpdated.getLaptopName());
                    return laptopRepository.save(laptop);
                }).orElseThrow(() -> new NotFoundException("Laptop not found!"));
    }


    /*update student by id and not update it's child (Laptop). Here,each laptop have to laptop id .
    input json will be like--->
        {
        "studentName": "Md. Sohel Rana",
        "studentMark": 4440,
        "laptop": [
            {"lid": 6,
             "laptopName": "Dell Pro-50"
            },
            {"lid": 4,
             "laptopName": "Mackbook Pro-50"
            },
            {"lid": 5,
             "laptopName": "vivo Pro-50"
            }
        ]
    }
    */
    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @Valid @RequestBody Student studentUpdated) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setStudentName(studentUpdated.getStudentName());
                    student.setStudentMark(studentUpdated.getStudentMark());
                    Set<Laptop> laptops = student.getLaptop();
                    if (studentUpdated.getLaptop() != null) {
                        for (Laptop lp : laptops) {
                            for (Laptop ulp : studentUpdated.getLaptop()) {
                                if (lp.getLid() == ulp.getLid()) {
                                    lp.setLaptopName(ulp.getLaptopName());
                                }
                            }
                        }
                        student.setLaptop(laptops);
                    }
                    return studentRepository.save(student);
                }).orElseThrow(() -> new NotFoundException("Student not found with id " + id));
    }



}

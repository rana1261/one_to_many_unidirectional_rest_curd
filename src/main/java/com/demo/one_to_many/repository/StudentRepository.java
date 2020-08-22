package com.demo.one_to_many.repository;

import com.demo.one_to_many.model.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.one_to_many.model.Student;

import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


}

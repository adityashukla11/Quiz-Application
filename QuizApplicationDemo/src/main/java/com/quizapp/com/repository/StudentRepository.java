package com.quizapp.com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizapp.com.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long>{

}

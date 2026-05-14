package com.example.tp6.data.local

import com.example.tp6.data.model.Student
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val dao: StudentDao) {

    val allStudents: Flow<List<Student>> = dao.getAllStudents()

    suspend fun insert(student: Student)  = dao.insert(student)
    suspend fun update(student: Student)  = dao.update(student)
    suspend fun delete(student: Student)  = dao.delete(student)

    suspend fun getById(id: Int): Student? = dao.getStudentById(id)
}

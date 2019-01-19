package Service;
import java.util.List;

import Model.Student;


public interface IStudentService {

	List<Student> getAllStudents();
	
	void saveStudent(final Student student);

	void commitStudents();
	
	boolean updateStudent(Student student);
	boolean deleteStudent(Student student);

}

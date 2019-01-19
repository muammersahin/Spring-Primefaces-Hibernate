package Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Model.Student;

public class RandomStudentService implements IStudentService {

	Configuration configuration = new Configuration();
	SessionFactory factory;
	Session session;

	private static final Logger logger = LoggerFactory.getLogger(RandomStudentService.class);

	private List<Student> savedStudents;
	public static byte[] image;
	public static String city;

	public RandomStudentService() {
		savedStudents = new ArrayList<>();
		configuration.configure("hibernate.cfg.xml");
		factory = configuration.buildSessionFactory();
		session = factory.getCurrentSession();
		session.getTransaction().begin();
	}

	@Override
	public List<Student> getAllStudents() {
		@SuppressWarnings("unchecked")
		final List<Student> list = (List<Student>) session.createCriteria(Student.class).list();

		logger.info("Returning all the students with size {}", list.size());
		return list;
	}

	@Override
	public void saveStudent(Student student) {
		student.setStudentNumber(getRandomId());
		java.sql.Blob blob = null;
		try {
			blob = new javax.sql.rowset.serial.SerialBlob(image);
		} catch (Exception e) {

		}
		student.setImage(blob);
		student.setCity(city);
		blob = null;
		city = null;
		savedStudents.add(student);
		session.save(student);
		logger.info("Student is saved: {}", student);
	}

	private String getRandomId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	@Override
	public void commitStudents() {
		session.getTransaction().commit();
		configuration.configure("hibernate.cfg.xml");
		session = factory.getCurrentSession();
		session.getTransaction().begin();

	}

	@Override
	public boolean updateStudent(Student student) {

		Student obj = (Student) session.load(Student.class, student.getStudentId());
		session.update(obj);

		return true;

	}

	@Override
	public boolean deleteStudent(Student student) {

		Student obj = (Student) session.load(Student.class, student.getStudentId());
		session.delete(obj);

		return true;

	}

}

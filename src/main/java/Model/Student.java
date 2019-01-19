package Model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Entity
public class Student implements Serializable {

	private static final long serialVersionUID = 1L;

	private long studentId;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String studentNumber;
	public String studentName, studentSurname;
	public String phoneNumber, city, distinct;
	public Blob image;
	public UploadedFile file;
	StreamedContent studentImage;
	
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}

	public StreamedContent getStudentImage() {
		return studentImage;
	}

	public void setStudentImage(StreamedContent studentImage) {
		this.studentImage = studentImage;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Student(String studentNumber, String studentName, String studentSurname, String phoneNumber, Blob image,
			UploadedFile file, StreamedContent studentImage) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
		this.studentSurname = studentSurname;
		this.phoneNumber = phoneNumber;
		this.image = image;
		this.file = file;
		this.studentImage = studentImage;
	}

	public Student() {
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentSurname() {
		return studentSurname;
	}

	public void setStudentSurname(String studentSurname) {
		this.studentSurname = studentSurname;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", studentNumber=" + studentNumber + ", studentName=" + studentName
				+ ", studentSurname=" + studentSurname + ", phoneNumber=" + phoneNumber + ", image=" + image.toString()
				+ ", file=" + file + ", studentImage=" + studentImage + "]";
	}

}

package WebApp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import Model.Student;
import Service.IStudentService;
import Service.RandomStudentService;

@Component
@ViewScoped
public class StudentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(StudentBean.class);

	@Autowired
	private IStudentService studentService;
	private Student student;

	private List<Student> students;

	private Student selectedStudent;
	private boolean res;
	private UploadedFile file;
	private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
	private Map<String, String> distincts;
	private Map<String, String> cities;
	private String city, distinct;

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}

	public Map<String, String> getCities() {
		return cities;
	}

	public void setCities(Map<String, String> cities) {
		this.cities = cities;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public StudentBean() {
		super();
	}

	@PostConstruct
	public void init() {
		this.student = new Student();
		this.students = studentService.getAllStudents();
		this.cities = new HashMap<String, String>();

		jsonParse();

		for (int i = 0; i < students.size(); i++) {
			int blobLength;

			try {
				blobLength = (int) students.get(i).getImage().length();
				byte[] blobAsBytes = students.get(i).getImage().getBytes(1, blobLength);
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(blobAsBytes));
				ImageIO.write(image, "JPG",
						new File("src/main/webapp/resources/" + students.get(i).getStudentNumber() + ".jpg"));
			} catch (Exception e1) {
			}

		}

	}

	public void upload(FileUploadEvent file) throws IOException {

		RandomStudentService.image = file.getFile().getContents();

	}

	public void save() throws IOException {

		if(student.getStudentName() != null && !student.getStudentName().equals("")) {
			logger.info("new student is going to be saved: {} ", this.student);
			this.studentService.saveStudent(student);
			this.init();
		}
		
	}

	public void commit() {
		this.studentService.commitStudents();
		this.init();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Başarılı", "Tüm Değişiklikler Kaydedildi!"));

	}

	public void delete(Student student) {
		this.studentService.deleteStudent(student);
		this.init();
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Başarılı", "Seçilen Öğrenci silindi!"));
	}

	public void onRowEdit(RowEditEvent event) throws ParseException {

		res = this.studentService.updateStudent(student);
		if (res) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Başarılı", "Güncelleme İşleminiz Tamamlandı!"));
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Başarısız", "Güncelleme İşleminiz Tamamlanamadı!"));
		}
	}

	public void onRowCancel(RowEditEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("İptal", "Güncelleme İşleminiz İptal Edildi!"));
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Student Selected", ((Student) event.getObject()).getStudentName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
		selectedStudent = (Student) event.getObject();
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Car Unselected", ((Student) event.getObject()).getStudentName());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public IStudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(IStudentService studentService) {
		this.studentService = studentService;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Student getSelectedStudent() {
		return selectedStudent;
	}

	public void setSelectedStudent(Student selectedStudent) {
		this.selectedStudent = selectedStudent;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void jsonParse() {

		String cityJsonStr = loadJSON();

		org.primefaces.json.JSONArray jsonArray;

		JSONObject jsonObject = new JSONObject(cityJsonStr);
		jsonArray = jsonObject.getJSONArray("cities");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject o = (JSONObject) jsonArray.get(i);
			String sehiradi = o.getString("il");
			cities.put(sehiradi, sehiradi);
			org.primefaces.json.JSONArray ilceler = o.getJSONArray("ilceleri");
			Map<String, String> map = new HashMap<String, String>();
			;
			for (int k = 0; k < ilceler.length(); k++) {
				map.put(ilceler.getString(k), ilceler.getString(k));
			}
			data.put(sehiradi, map);

		}

	}

	public String loadJSON() {
		String json = null;
		try {
			InputStream is = new FileInputStream(new File("cities.json"));

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");

		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	public void onCityChange() {
		RandomStudentService.city = city;
		if (city != null && !city.equals("")) {
			distincts = data.get(city);
		} else
			distincts = new HashMap<String, String>();
	}

	public void displayLocation() {
		FacesMessage msg;
		if (distinct != null && city != null)
			msg = new FacesMessage("Selected", distinct + " of " + city);
		else
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Distinct is not selected.");

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Map<String, String> getDistincts() {
		return distincts;
	}

	public void setDistincts(Map<String, String> distincts) {
		this.distincts = distincts;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}

}

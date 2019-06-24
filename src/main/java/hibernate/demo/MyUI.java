package hibernate.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.UI;
import hibernate.demo.entity.Course;
import hibernate.demo.entity.Instructor;
import hibernate.demo.entity.InstructorDetail;
import hibernate.demo.entity.Student;
import org.hibernate.Session;

import javax.servlet.annotation.WebServlet;
import java.util.*;

/** http://localhost:8080/Many-to-Many-1.0-SNAPSHOT/InstructorUIServlet
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        GridLayout layout = new GridLayout(4,4);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {



            List<Student> students = session.createQuery("from Student", Student.class).list();
            List<Course> courses = session.createQuery("from Course", Course.class).list();

            session.beginTransaction();


            Course firstCourse =
                    courses
                            .stream()
                            .min(Comparator.comparingInt(Course::getId))
                            .orElseThrow(NoSuchElementException::new);

            Student firstStudent =
                    students
                            .stream()
                            .min(Comparator.comparingInt(Student::getId))
                            .orElseThrow(NoSuchFieldException::new);


            firstCourse.addStudent(firstStudent);
            session.getTransaction().commit();

            session.beginTransaction();

            Course lastCourse =
                    courses
                            .stream()
                            .max(Comparator.comparingInt(Course::getId))
                            .orElseThrow(NoSuchElementException::new);

            Student lastStudent =
                    students
                            .stream()
                            .max(Comparator.comparingInt(Student::getId))
                            .orElseThrow(NoSuchFieldException::new);


            session.delete(lastCourse);
            session.delete(lastStudent);

            session.getTransaction().commit();


            students = session.createQuery("from Student", Student.class).list();
            List<StudentsWithCources> studentsWithCources = new ArrayList<>();

            for(Student row: students){
                List<Course> coursesOneStudent = row.getCourses();
                if(!courses.isEmpty()){
                    for (Course lesson : coursesOneStudent){
                        studentsWithCources.add(new StudentsWithCources(row.getFirstName(),row.getLastName(),lesson.getTitle()));
                    }
                }
            }

            Grid<StudentsWithCources> gridStudents = new Grid<>();
            gridStudents.setItems(studentsWithCources);
            gridStudents.addColumn(StudentsWithCources::getStudentFirstName).setCaption("First Name");
            gridStudents.addColumn(StudentsWithCources::getStudentLastName).setCaption("Last Name");
            gridStudents.addColumn(StudentsWithCources::getCourseTitle).setCaption("Course title");
            layout.addComponent(gridStudents,0,0,3,2);

            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        setContent(layout);

    }

    @WebServlet(urlPatterns = "/*", name = "InstructorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }



}

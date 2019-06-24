package hibernate.demo;

public class StudentsWithCources {

    private String studentFirstName;

    private String studentLastName;

    private String courseTitle;

    public StudentsWithCources(String studentFirstName, String studentLastName, String courseTitle) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.courseTitle = courseTitle;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }


    public String getStudentLastName() {
        return studentLastName;
    }


    public String getCourseTitle() {
        return courseTitle;
    }

}

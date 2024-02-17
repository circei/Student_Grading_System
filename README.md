# University Management App

The University Management App is a Java application developed using JavaFX for the user interface and PostgreSQL for the database backend. This application allows students and teachers to manage academic information such as grades, subjects, and profiles.

## Features

- **User Authentication**: Both students and teachers can log in securely to access their respective functionalities.
- **Student Features**:
  - View Grades: Students can view their grades for various subjects.
  - View Subjects: Students can see the subjects they are enrolled in.
  - View Profile: Students can view their personal profile information.
- **Teacher Features**:
  - Add Grades: Teachers can add grades for students.
  - View Profile: Teachers can view their personal profile information.
  - View Student Grades: Teachers can see the grades of individual students.

## Technologies Used

- **JavaFX**: JavaFX is used for building the graphical user interface of the application, providing a rich set of UI components for a seamless user experience.
- **PostgreSQL**: PostgreSQL is utilized as the relational database management system for storing and managing academic data, including student profiles, grades, and subjects.

## How to Run

1. Clone the repository to your local machine.
2. Ensure you have Java and PostgreSQL installed on your system.
3. Set up the database by executing the provided SQL scripts in the `database` directory.
4. Update the database connection settings in the Java code to match your PostgreSQL configuration.
5. Compile and run the application using your preferred Java IDE or command line tools.

## Database Schema

The database schema consists of the following tables:

- `students`: Stores information about students, including usernames, passwords, and personal details.
- `teachers`: Stores information about teachers, including usernames, passwords, and personal details.
- `subjects`: Stores information about subjects offered by the university.
- `student_grades`: Stores grades for each student, along with the corresponding subject and date.

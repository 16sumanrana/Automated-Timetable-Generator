# Automated-Timetable-Generator
A java application to generate timetables.

It is a NP-Complete problem and Genetic Algorithm is used to generate best timetable according to constraints.

Constraints:
  1. Two teachers can't be assigned to the same period for a class in some day.
  2. A teacher can't be assigned to two different classes at same time in some day.
  3. For two consicutive periods, two subjects can't be same.
  4. Maximum 2 periods is allowed for a teacher for some class in a day.
  
Some assumptions:
  1. There is not blank period for a class in some day.
  2. There are enough teachers such that all constrains will be satisfied.
  
Procedure:
  1. Genetic algorithm is used.
  2. At first 10 schedules are created randomly and they are sorted according to their no of conflicts.
  3. From 10 schedules 50% new schedules are created by crossover using roulette wheel selection.
  4. 25% new schedules are created by mutation.
  5. Remaining 25% new schedules are created.
  6. From 20 schedules 10 best schedules are selected for next generation.
  7. (3, 4, 5, 6) steps are continued until no of conflicts of any schedule becomes 0.
  8. Maximum 50000 generation is allowed.
  
More features:
  1. JAVA is used as programming language to deploy this application.
  2. JSP is used to create user interface.
  3. JDBC is also used to store and manipulate data.
  4. MySQL DB is used here for DB server.
  5. Tomcat 9.0.17 is used as localserver.
  6. Bootstrap 4 is for UI design.
  7. HTML, CSS, JavaScript and Ajax are basic components.
  
User interface:
  1. index.jsp:
    Here last generated timetables will be shown.
    ![index](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/index.png)
    
  2. generate.jsp:
    Timetable will be generated here and generation status will be shown.
    ![generate table](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/generate.png)
    
  3. class.jsp:
    All class information will be displayed and class can be added, edited and also deleted.
    ![class](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/class.png)
   
  4. day.jsp:
    Days information will be displayed.
    ![day](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/day.png)
    
  5. time.jsp:
    Information about timeslots will be displayed.
    ![timeslots](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/time.png)
  
  6. subject.jsp:
    All subjects that are offered by the institute will be shown.
    ![subjects](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/subject.png)
    
  7. classwisesubject.jsp:
    Subjects according to classes is shown. Subjects can be added, edited and deleted.
    ![classwisesubjects](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/classwisesubject.png)
    
  8. teacher.jsp:
    All teachers information will be shown.
    ![teachers](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/teacher.png)
    
  9. map.jsp:
    Here teachers are assigned to subjects according to classes. For any subject in a class no other teachers are assigned in timetable other than these specified teachers.
    ![map](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/img/map.png)
    
References:
  1. [https://www.youtube.com/watch?v=cn1JyZvV5YA&ab_channel=zaneacademy](https://www.youtube.com/watch?v=cn1JyZvV5YA&ab_channel=zaneacademy)
  2. [https://www.youtube.com/watch?v=frB2zIpOOBk&ab_channel=AskFaizan](https://www.youtube.com/watch?v=frB2zIpOOBk&ab_channel=AskFaizan)
  
Todo list:
  1. Make this application more efficient.
  2. Use this application capable to generate timetables for many institutes.
  
  
  Efficient approches are welcomed.

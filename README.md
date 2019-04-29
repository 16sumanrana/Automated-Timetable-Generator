# Automated-Timetable-Generator
A java application to generate timetable.

It is a NP-Complete problem and Genetic Algorithm is used to generate best timetable according to constraints.

Constraints:
  1. Two teachers can't be assigned to the same period, same class in some day.
  2. A teacher can't be assigned to two different classes at same time in some day.
  3. For two consicutive periods, subjects can't be same.
  4. Maximum 2 periods is allowed for a teacher for some class.
  
Some assumptions:
  1. There is not blank period for a class in some day.
  2. There are enough teachers such that all constrains will be satisfied.
  
Procedure:
  1. Genetic algorithm is used to generate timetable.
  2. At first 10 schedules are created randomly and they are sorted according to their no of conflicts.
  3. From 10 schedules 50% new schedules are created by crossover.
  4. 25% new schedules are created by mutation.
  5. Remaining 25% new schedules are created.
  6. From 20 schedules 10 best schedules are selected for next generation.
  7. (2, 3, 4, 5) steps are continued until no of conflicts of any schedule becomes 0.
  8. Maximum 50000 generation is allowed.
  
More features:
  1. JAVA is used as programming language to deploy this application.
  2. JSP is used for user interface.
  3. JDBC is also used to store and manipulate data.
  4. MySQL DB is used here for DB server.
  5. Tomcat 9.0.17 is used as localserver.
  6. Bootstrap 4 is for UI design.
  
User interface:
  1. index.jsp:
    Here last generated timetable will be shown.
    ![alt index](https://github.com/16sumanrana/Automated-Timetable-Generator/blob/master/index.png)
    
  2. 

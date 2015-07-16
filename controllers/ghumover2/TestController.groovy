package ghumover2

import grails.converters.JSON

import java.text.DateFormat
import java.text.SimpleDateFormat

class TestController {


    def index() {

          JSON.use('TeachersSubjects'){
         //  render Teacher.findByUsername(params.id).getAllGradesAndSubjects() as JSON


       render Teacher.findByUsername(params.id).getSubjectsInGrade(Grade.findByNameAndSection(Integer.parseInt(params.g),params.s)) as JSON
          }
    }

   def allEvents()
      {

          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

          Date date = formatter.parse("12-04-2015");
          CalendarDate dt =  CalendarDate.findByCalendar_date(date)



          render Event.findAllByDate(dt.calendar_date) as JSON
      }

    def allGradeEvents()
      {
          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

          Date date = formatter.parse("12-04-2015");
          CalendarDate dt =  CalendarDate.findByCalendar_date(date)
          render Grade.findByNameAndSection(5,"A").events as JSON
      }


    def s()
    {

      render new Grade(name: 12 , section: "A").save()
      //  render new Grade(name: 6 , section: "B").save()
    }
    def l()
    {

           render        SchoolClass.findByClassName(Integer.parseInt(params.id)).grades as JSON
    }
    def n()
    {
        render new SchoolClass(className: 5).save()
    }

    def results()
    {

       render  Exam.get(1) as JSON

    }

    def exams()
    {

        render  Exam.get(1) as JSON

    }

    def testtime()
    {


        String input = "2014-04-25 02:03:13";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("'Date : 'dd-MM-yyyy\n'Time : 'KK:mm a");
        render (outputFormat.format(inputFormat.parse(input)));

    }
}

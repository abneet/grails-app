package ghumover2

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
@Secured(['ROLE_TEACHER','ROLE_PARENT'])
class TimeTableController {

   // static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def springSecurityService

    User user


    def getWeekTimetable()
    {

                def gradeName = params.gradeId
                def response = [:]
                def section = params.section
                def grade = Grade.findByNameAndSection(gradeName,section)
                def timetable = TimeTable.findAllByGrade(grade)
                def days = TimeTable.executeQuery("select distinct a.day from TimeTable a where a.grade = ? " , [grade])
               JSON.use('getTimeTable')
                       {
                                    days.each {
                                         response[it] = TimeTable.findAllByGradeAndDay(grade,it)
                                    }

                                    render response as JSON
                       }



                 }


    def getDayTimeTable()
             {

                 def day = params.day
                 def section = params.section
                 JSON.use('getTimeTable')
                         {
                             def grade = Grade.findByNameAndSection(params.gradeId, params.section)
                             def result = TimeTable.findAllByGradeAndDay(grade, day)
                             render result as JSON
                         }


             }







    def getTeacherWeekTimetable()
      {
          def output = [:]
          try {

                user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                Teacher teacher = Teacher.findByUsername(user.username)
                JSON.use('teacherWeekTT')
                        {
                            output['teacherId'] = teacher.id.toString()
                            output['teacherName'] = teacher.teacherName
                            output['timeTable'] = teacher.timetables
                            render output as JSON
                        }
            }
            catch (Exception e)
            {
                 render e
            }
      }





    def getTeacherDayTimetable()
     {
         def output = [:]
         try {
             user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
             Teacher teacher = Teacher.findByUsername(user.username)
             String day = params.day
             output['teacherId'] = teacher.id.toString()
             output['teacherName'] = teacher.teacherName
             output['timeTable'] =  TimeTable.findAllByTeacherAndDay(teacher,day)
             render output as JSON

         }
         catch (Exception e)
         {
                render e
         }

      }












}

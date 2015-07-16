package ghumover2

import grails.converters.JSON

import java.text.SimpleDateFormat

class EventDetailsController {

    def index() {}

    def saveEvent()
    {
         def output = [:]
        try {

            CalendarDate calendarDate;
            Date date
            String title
            String description
            String startime
            String endTime
            String flag
            Integer gradeId

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            date = formatter.parse(params.date);
            calendarDate =  CalendarDate.findByCalendar_date(date)

            title = params.title
            description = params.description
            startime = params.startTime
            endTime = params.endTime
            flag = params.flag


            def save


            if(params.grade)
            {
             gradeId = Integer.parseInt(params.grade);
             Grade grade = Grade.get(gradeId)
             save =  new Event(calendar_date: calendarDate,title: title,description: description,startTime: startime,endTime: endTime,flag: flag,grade: grade).save()
            }
            else
            {
                save = new Event(calendar_date: calendarDate,title: title,description: description,startTime: startime,endTime: endTime,flag: flag).save()
            }
           if(save)
           {

              output['data'] = save
              output['status'] =  "success"
           }
           else
           {

               output['data'] = save
               output['status'] =  "error"

           }
            render output as JSON

        }
       catch (Exception e)
       {
           render e as JSON

       }
    }
}

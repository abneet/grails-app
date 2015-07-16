package ghumover2

import grails.converters.JSON
import grails.rest.Resource

import java.text.SimpleDateFormat


@Resource(formats=['json', 'xml'])
class Grade {


    Long gradeId
    int name
    String section
    Long classTeacherId
    SchoolClass schoolClass
	String gradetags
    static hasMany = [students:Student,exams:Exam , events:Event , attendance:Attendance , teachers:Teacher , timetable:TimeTable ]
    static belongsTo = [schoolClass:SchoolClass]




    static mapping ={
	id generator: 'increment',name: 'gradeId'
	students cache :true
	attendance cache :true

    }

    static constraints = {

        classTeacherId(nullable: true)
        name(unique: 'section')
        schoolClass(nullable: true)
           gradetags(nullable: true)
    }
    def beforeInsert(){
		if(null == schoolClass){
        def s = SchoolClass.findByClassName(name)
        if(!s)
        {
            def sg = new SchoolClass()
            sg.className= name


            SchoolClass.withNewSession{
                if(sg.save(flush: true))
                {
                    schoolClass = SchoolClass.findByClassName(name)
                }

            }


        }
        else {
            schoolClass = s
        }
}

    }
	
	def afterInsert = {
		gradeId = this.gradeId;
		
	}





    def addSubjectAndTeacher(Subject subject , Teacher teacher)
      {

          new GradeTeacherSubject(grade: this , teacher:teacher , subject:subject).save()
		  teacher.addToGradeSubject(this , subject)
		  this.addSubjectAndTeacher(subject,teacher)

      }
	  
	  def getSubjectAndTeacher(Subject subject , Teacher teacher)
	  {

		  return GradeTeacherSubject.findAllByGrade(this)

	  }



     def getAttendance(String date)
     {

         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
         Date att_date = formatter.parse(date);
         return  Attendance.findAllByDateAndGrade(att_date,this)
     }

    def getClassTeacher()
    {
        return (this.classTeacherId) ? Teacher.findById(this.classTeacherId) : null ;
    }

}

package ghumover2

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService;

import org.joda.time.DateTime

import java.text.SimpleDateFormat

import javax.mail.Session;

class AttendanceController {

	static allowedMethods = [saveAttendance	: "POST"]
	def index() {}


	def saveAttendance()
	 {
		 def output = [:]
		 try {

			 String date = params.date
			 int gradeId = Integer.parseInt(params.grade)
			 String section = params.section
			 Grade grade = Grade.findByNameAndSection(gradeId,section)
			 String present_flag = params.present_flag


						 if(present_flag !="P" && present_flag !="A")
						  {
									  output['status'] = 'error'
									  output['message'] = 'Invalid flag value'
									  output['data'] = "Failed to save"
									  render output as JSON


						  }
					  else
						 {


							 // Check if already an attendance entry for same day and for same class , if yes , remove it first because it may be a re-entry of same date
							 SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
							 Date att_date = formatter.parse(date);
							 (Attendance.findByGradeAndDate(grade,att_date)) ? (Attendance.findByGradeAndDate(grade,att_date)).delete(flush: true) : null  ;


							 Attendance a = new Attendance(grade: grade , date:date)
												 if(present_flag == "A")
												 {
													 Student tempStudent
													 params.studentList.each { studentId ->
														 tempStudent = Student.get(Long.parseLong(studentId))
														 a.addToAbsentees(tempStudent)
													 }
													 a.all_present = false;
													 a.save()
													 JSON.use('absentees')
															 {
																 output['data'] = a
																 output['status'] = 'success'
																 output['message'] = 'Successfully added attendace entry'

																 render output as JSON }
												 }
												 else if (present_flag == "P" )
												 {
													 a.all_present = true
													 a.save()


																 output['status'] = 'success'
																 output['message'] = 'Successfully added attendace entry'
																 output['data'] = "All students present"
																 render output as JSON
												 }


						 }


		 }
		 catch (Exception e)
		 {


			 render "Error occured check your input keys and values"
		 }

	 }
	 SpringSecurityService springSecurityService

	 def getGradeAttendanceV1(){
		 
		 
				 def output  = [:]
				  try {
		 
					  String date = params.date
					  int gradeId = Integer.parseInt(params.grade)
					  String section = params.section
					  
					  User user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
					  

					  
					  if(Grade.findByClassTeacherId(user.id)!=null){
					  
					 
						  
					 
					  println "------------------------------------ [${user.id}]"
					
			
					  
					 
					def   attendance=  Grade.findByClassTeacherId(user.id).getAttendance(date)
					  if(attendance){
					  JSON.use('absentees'){
					
						 
						  render attendance as JSON
					  }
					  }else {
					  
						  def	 grade= Grade.findByClassTeacherId(user.id)
						if(grade!=null){
							JSON.use('notAttendance'){
							def grd =[]
							grd<<grade
								render  grd as JSON
						}
					  }
					  }
				 }else{
						
					def responseObj=[]
						output['status'] = "Info"
						output['message'] = "No Class assigned as Class Teacher"
						responseObj<<output
						render responseObj as JSON
					 }
					
				  }
				  catch (Exception e)
				  {
					  render e
				  }
			 
	 }
	 
	def getGradeAttendance()
	{

		def output  = [:]
		 try {

			 String date = params.date
			 int gradeId = Integer.parseInt(params.grade)
			 String section = params.section
			 def attendance= Grade.findByNameAndSection(gradeId,section).getAttendance(date)
			 if(attendance){
			 JSON.use('absentees'){
				attendance= Grade.findByNameAndSection(gradeId,section).getAttendance(date) as JSON
				
				 render attendance
			 }
			 }else {
			 JSON.use('notAttendance'){
				def	 grade= Grade.findByNameAndSection(gradeId,section) as JSON
			   
				render grade
			}
		
			 
			 }





		 }
		 catch (Exception e)
		 {
			 render e
		 }
	}



	def getStudentAttendanceBetween()
	  {
		  def output = [:]

			try {

				Student student = Student.get(Long.parseLong(params.studentId))
				def absentDays = student.getAttendance(params.fromDate , params.toDate)
				def absentDates = []
				absentDays.each {
					absentDates << it.date?.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				}
				output['studentId'] = student.studentId.toString()
				output['studentName'] = student.studentName
				output['fromDate'] = params.fromDate
				output['toDate'] = params.toDate
				output['total_working_days'] = "X"
				output['total_present_days'] = "X"
				output['total_absent_days'] = absentDays.size()
				output['absent_days'] = absentDates
				render output as JSON




			}
			catch (Exception e)
			{
				render e
			}

	  }





	def getAttendanceOfMonth()
	   {

		   def output = [:]

		   try {
			   int year = Integer.parseInt(params.year)
			   int month = Integer.parseInt(params.month)
			   Student student = Student.get(Long.parseLong(params.studentId))

			   DateTime dateTime = new DateTime(year, month, 1, 0, 0, 0, 0);
			   int daysInMonth = dateTime.dayOfMonth().getMaximumValue();

			   Integer workingDays = CalendarDate.getTotalWorkingDays(month,year)


			   String from_date = "01-" + month + "-" + year;
			   String to_date = daysInMonth + "-" + month + "-" + year

			   def absentDays = student.getAttendance(from_date, to_date)
			   def absentDates = []
			   absentDays.each {
				   absentDates << it.date?.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			   }
			   output['studentId'] = student.studentId.toString()
					   output['studentName'] = student.studentName
					   output['fromDate'] = from_date
					   output['toDate'] = to_date
					   output['absent_dates'] = absentDates
					   output['total_absent_days'] = absentDays.size().toString()
					   output['total_working_days'] = workingDays.toString()
					   output['present_days']=(workingDays-absentDays.size()).toString()

			   render output as JSON
		   }
		   catch (Exception e)
		   {

			   render e
		   }

	   }


}

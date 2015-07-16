package ghumover2

import grails.converters.JSON
import grails.rest.RestfulController
import grails.plugin.springsecurity.annotation.Secured

import java.text.SimpleDateFormat;
import java.util.Set;

@Secured(['ROLE_PARENT'])
class GuardianController extends RestfulController
{

	static responseFormats = ['json', 'xml']
	def springSecurityService
	User user
	private static final String ROLE_TEACHER = 'ROLE_TEACHER'
	private static final String ROLE_PARENT = 'ROLE_PARENT'
	private static final String ROLE_ADMIN = 'ROLE_ADMIN'
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	GuardianController() {
		super(Guardian)
	}


	def getAccountInfo()
	{
		 def id = params.id.toString()



					 Guardian g = (id.isNumber()) ? Guardian.findById(id) : Guardian.findByUsername(id);
					 def guardian = [:]
					 def output = [:]


					 guardian['username'] = g.username
					 guardian['name'] = g.name
					 guardian['educational_qualification'] = g.educational_qualification
					 guardian['profession'] = g.profession
					 guardian['designation'] = g.designation
					 guardian['mobileNumber'] = g.designation
					 guardian['emailId'] = g.emailId
					 guardian['officeNumber'] = g.officeNumber
					 guardian['numberOfChildren'] = g.getChildren().size()
					 output['accountInfo'] = guardian

								JSON.use('getChildren')
										{    def children = g.getChildren()
											output['children'] = children
											render output as JSON

										}






	}

	 def getAllChildren()
	 {
		 def response = [:]
			 try {
						  def id = params.id
						  def output = [:]
						  Guardian  g =   (id.isNumber()) ? Guardian.findById(id) : Guardian.findByUsername(id);
						  def children = g.getChildren()
						  output['numberOfChildren'] = children.size()
						 JSON.use('getChildren')
						  {
							   output['children'] = children
							  render output as JSON
						  }



				 }
			catch (Exception e)
			{
				response['status'] = "error"
				response['message'] = e
				render response as JSON

			}
	 }


	def getStudentClassEvents()
	       {
              def result = [:]
			   try {



				   Date date = formatter.parse(params.date);
                   Long sid = Long.parseLong(params.studentId)
				   Student student = Student.findByStudentId(sid)


				   def eventList = Event.findAll("from Event as e where (e.calendar_date.calendar_date = :date and e.grade.gradeId = :gradeId ) or (e.calendar_date.calendar_date = :date and e.flag = :flag) order by e.calendar_date.calendar_date ",[date:date ,gradeId:student.grade.gradeId,flag:"SCHOOL"  ]  )


				   result['studentId'] = student.studentId.toString()
				   result['teacherName'] = student.studentName
				   result['eventDate'] = date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				   result['no_of_events'] = eventList.size().toString()
				   result['events'] = eventList
			       render result as JSON
			   }
              catch (Exception e)
			  {
				  render e
			  }


	        }



	def getStudentMonthEvents()
	  {
	     def result = [:]
		 try {


			 int month = Integer.parseInt(params.month)
			 int year =  Integer.parseInt(params.year)
			 Long sid = Long.parseLong(params.studentId)
			 Student student = Student.findByStudentId(sid)

			 Date start_date = formatter.parse("01-"+month+"-"+year)
			 Date end_date = formatter.parse(CalendarDate.getTotalDaysInMonth(month,year)+"-"+month+"-"+year)


			 def eventList = Event.findAll("from Event as e where (e.calendar_date.calendar_date between :f_date and :t_date  and e.grade.gradeId = :gradeId)  or  ( e.calendar_date.calendar_date between :f_date and :t_date  and e.flag = :flag) order by e.calendar_date.calendar_date " , [f_date:start_date , t_date:end_date , gradeId:student.grade.gradeId , flag:"SCHOOL" ] )

			 result['studentId'] = student.studentId.toString()
			 result['studentName'] = student.studentName
			 result['month'] = month.toString()
			 result['year'] = year.toString()
			 result['from_date'] = start_date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			 result['to_date'] = end_date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			 result['no_of_events'] = eventList.size().toString()
			 result['events'] = eventList
			 render result as JSON


		 }
		catch (Exception e)
		{
             render e

		}

	  }


def getStudentList(){
	user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
	def email =[]
	Teacher t = Teacher.findByUsername(user.username)
	def grades=t.grades
	t.grades.each {
	it.students.each {
		email <<it.father
		email<<it.mother
		
		
	}
		
	render email	
	}
	
}


	def getTeacherList()
	   {
		     try {

				 user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
				 	Set<Role> roles=user.getAuthorities()
					 String role
					 roles.each {
						  role=it.authority
					 }
					 if(role.equalsIgnoreCase(ROLE_PARENT)){
				 
				 def teachers =[]
				Guardian g= Guardian.findByUsername(user.username)
				g.getChildren().each {
					def grd=it.grade.teachers
					
						teachers<<grd
					
					
					
				 }
				 JSON.use('TeacherListForParent'){
				 render teachers as JSON
				 }
					 }else {
					 
					 def parents =[]
					 Teacher t = Teacher.findByUsername(user.username)
					 Grade grade=Grade.findByClassTeacherId(t.id)
					
					 grade.students.each {
						 parents <<it.father
						 parents<<it.mother
						 
						 
					 
					 }
					 JSON.use('ParentListForTeacher'){
						 render parents as JSON
						 }

			 }
		     }
			 catch (Exception e)
			 {
				 render e
			 }

	   }


}
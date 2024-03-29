package ghumover2

import grails.converters.JSON
import grails.rest.RestfulController

import java.text.SimpleDateFormat
import java.util.List

import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.rest.client.RestBuilder


@Secured(['ROLE_TEACHER'])
class TeacherDetailsController extends RestfulController {
	static responseFormats = ['json', 'xml']

	static  scaffold = true
	def springSecurityService
	User user
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	static allowedMethods = [sendMailToParents	: "POST"]
	private static final String ROLE_TEACHER = 'ROLE_TEACHER'
	
	TeacherDetailsController() {
		super(Teacher)
	}
	
	def forgetPassowrd(){
		String emailId = params.emailId
	//	String newPassword = params.password_new
		println "test this {{emailId}}"
		def message
		User user=User.findByUsername(emailId)
		if(user){
			user.password="1234"
			user.save()
			//sendMail
			message="password sent to registered mail id"
		}else{
		
		message="email id is not valid"
		}
		
		render message
	}
	
	def getGrade (){
		def article=new Grade()
		def articleList=article.list()
		println articleList


		JSON.use('thin') { render articleList as JSON }
	}

	def getHomeWork (){
		def article=new Homework()
		def articleList=article.list()
		println articleList


		JSON.use('homework') { render articleList as JSON }
	}
	
	

	def getTeacherDetails (){
		def article=new Teacher()
		String grade= params.userId

		def trek=article.findAllWhere(username:grade).first()

		JSON.use('teacherC') { render trek as JSON }
	}

	def getMsg (){
		def msgType=new Message()

		def trek=msgType.findAllWhere(type:"msg")

		JSON.use('msg') { render trek as JSON }
	}


	def getSubject (){



		def output = [:]
		def subjects = [:]
		user  =   springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
		Teacher t = Teacher.findByUsername(user.username)
		Grade grade = Grade.findByNameAndSection(Integer.parseInt(params.grade),params.section)


		JSON.use('TeachersSubjects'){
			output['teacherId'] = t.id.toString()
			output['username'] = user.username
			output['grade'] = grade.name
			output['section'] = grade.section
			output['subjects'] = t.getSubjectsInGrade(grade)
			render output as JSON
		}

	}

	def getStudentList (){
		def article=new Student()
		Long grade= Long.parseLong(params.gradeId)

		def trek=article.findAllWhere('grade.gradeId':grade)
		//render trek as JSON


		JSON.use('student') { render trek as JSON }



	}

	def getStudentListByGradeSection()
	{
		def article=new Student()
		int grade=  Integer.parseInt(params.grade)
		String section = params.section
		
		def trek=Student.findAll("from Student as s where s.grade.name = ? and s.grade.section = ? ",[grade,section])
		//render trek as JSON


		JSON.use('student') { render trek as JSON }

	}


	def sendMessage(){
		/*	//JSON Object is not bound to params it is bound to rehquest in POST/PUT
		 def jsonObj = request.JSON
		 def catalogParams = [] as List
		 jsonObj.student.each{
		 catalogParams << new Student(it)
		 }
		 //Set the domain back to the jsonObj
		 jsonObj.parametros = catalogParams
		 //Bind to catalog
		 def stud = new Student(jsonObj)
		 //Synonymous to new Catalog(params) but here you cannot use params.
		 //Save
		 if (!stud.save(flush: true)){
		 stud.errors.each {
		 println it
		 }
		 }
		 render stud
		 }*/

		def jsonObj = request.JSON
		def stud
		for (int i = 0; i < jsonObj.size(); i++) {
			stud = new Homework(jsonObj[i])


			if (!stud.save(flush: true)){
				stud.errors.each {
					println it
					render failure as JSON
				}
			}
		}

		render stud as JSON
	}

	def getTeacherEvents()
	{
		def output = [:]
		try {

			user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
			Teacher t = Teacher.findByUsername(user.username)
			def teacherGrades = t.grades
			Date date = formatter.parse(params.date)
			if(null!=teacherGrades && teacherGrades.size()>0){

			def teacherEvents = Event.findAll("from Event as e where (e.calendar_date.calendar_date = :date and e.grade.gradeId in (:g_list)) or (e.calendar_date.calendar_date = :date and e.flag = :flag ) order by e.calendar_date.calendar_date   " ,[date:date , g_list: t.grades.gradeId , flag:"SCHOOL"])
		    if(null!=teacherEvents && teacherEvents.size()>0){
				teacherEvents.each {
				if(it==null){
					output['status'] = 'error'
					output['message'] = ' No Event found for Teacher '
					render output as JSON
				}
				}
				output['teacherId'] = t.id.toString()
			output['teacherName'] = t.teacherName
			output['eventDate'] = date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			output['no_of_events'] = teacherEvents.size().toString()
			output['events'] = teacherEvents
            if(output.size()>0 && output.values()!=null){
			render output as JSON
            }
		    }}
				output['status'] = 'error'
				output['message'] = ' No Event found for Teacher '
				render output as JSON
			
				   

		}
		catch (NullPointerException ne)
		{
			println ne
			render(ne)
		}
		catch (Exception e)
		{
			println e
			render e
		}



	}



	def getTeacherMonthEvents()
	{
		def output= [:]
		try {
			user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
			Teacher t = Teacher.findByUsername(user.username)



			int month = Integer.parseInt(params.month)
			int year =  Integer.parseInt(params.year)


			Date start_date = formatter.parse("01-"+month+"-"+year)
			Date end_date = formatter.parse(CalendarDate.getTotalDaysInMonth(month,year)+"-"+month+"-"+year)


			def eventList = Event.findAll("from Event as e where (e.calendar_date.calendar_date between :f_date and :t_date  and e.grade.gradeId in (:g_list))  or  ( e.calendar_date.calendar_date between :f_date and :t_date  and e.flag = :flag) order by e.calendar_date.calendar_date " , [f_date:start_date , t_date:end_date , g_list:t.grades.gradeId , flag:"SCHOOL" ] )

			output['teacherId'] = t.id.toString()
			output['teacherName'] = t.teacherName
			output['month'] = month.toString()
			output['year'] = year.toString()
			output['from_date'] = start_date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			output['to_date'] = end_date.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
			output['no_of_events'] = eventList.size().toString()
			output['events'] = eventList

			render output as JSON

		}
		catch (NullPointerException ne)
		{
			println ne
			render(ne)
		}
		catch (Exception e)
		{
			render(e)

		}


	}





	def getAllSubjectsInAllGrades()
	{
		def output = [:]
		def subjects = [:]
		user  =   springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
		Teacher t = Teacher.findByUsername(user.username)
		JSON.use('TeachersSubjects')
				{
					output['teacherId'] = user.id.toString()
					output['username'] = user.username
					output['gradesAndSubjects'] = t.getAllGradesAndSubjects()
					render output as JSON
				}
	}


	def sendMailToParents()
	{


		try {

			String message  = params.message
			int gradeId = Integer.parseInt(params.grade)
			String section = params.section
			String title = params.title
			Conversation tempConv
			user  =   springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
			Teacher t = Teacher.findByUsername(user.username)
			Grade grade = Grade.findByNameAndSection(gradeId,section)
			def output = [:]
			def data = [:]
			grade.students.each {

				if(it.getFather()!= null){
					tempConv = new Conversation(fromId: it.getFather().username, toId:t.username , title: title , inTrash: false , isRead: false , toDate: new Date())
							.addToMessages(new Message(messageText:message , messageTime: new Date() , fromId: t.teacherName , toId: it.getFather()?.name))
							.save()
					it.getFather().addToConversations(tempConv).save()
					t.addToConversations(tempConv).save()

				}

				if(it.getMother()!= null)
				{
					tempConv = new Conversation(fromId: t.username , toId: it.getMother()?.username , title: title , inTrash: false , isRead: false , toDate: new Date())
							.addToMessages(new Message(messageText:message , messageTime: new Date() , fromId: t.teacherName , toId: it.getMother()?.name))
							.save()
					it.getMother().addToConversations(tempConv).save()
					t.addToConversations(tempConv).save()

				}

				if(it.getLocalGuardian()!= null)
				{
					tempConv =  new Conversation(fromId: t.username , toId: it.getLocalGuardian()?.username , title: title , inTrash: false , isRead: false , toDate: new Date())
							.addToMessages(new Message(messageText:message , messageTime: new Date() , fromId: t.teacherName , toId: it.getLocalGuardian()?.name))
							.save()
					it.getLocalGuardian().addToConversations(tempConv).save()
					t.addToConversations(tempConv).save()
				}
			}


			data['title'] = title
			data['message'] = message

			output['status'] = "success"
			output['message'] = "Message successfully sent to parents of "+grade.name +" "+section+" students"
			output['data'] = data

			render output as JSON



		}
		catch (Exception e)
		{
			render e
		}




	}



	def getTeacherWeekTimetable()
	{

		try {

			user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
			Teacher teacher = Teacher.findByUsername(user.username)



			def teacherTT = [:]

			def days = TimeTable.executeQuery("select distinct a.day from TimeTable a ")
			JSON.use('TeachergetTimeTable')
					{
						days.each {
							teacherTT[it] = TimeTable.findAllByTeacherAndDay(teacher,it)
						}

						render teacherTT as JSON
					}

		}
		catch (Exception e)
		{
			render e
		}
	}






	def addTeacher() {
		Map ob = new HashMap();

		try{
			Teacher t = new Teacher()

			Long userId = User.createCriteria().get {
				projections { max "id" }
			} as Long
			if(null == userId)
				userId =1
			else
				userId = userId+1
		   // t.school_id = Long.parseLong(params.school_id)
			t.school_id = 1
			t.teacherName = params.teacherName
			t.teacherPhoto ="test1"
			t.teacherEmailId =params.teacherEmailId
			t.phoneNo =params.phoneNo
			t.username= params.teacherEmailId
			t.password= "123"
			t.teacherId = userId
			t.deviceToken = "fsdjhsdf"
			t.save(flush:true);
			def rol=Role.findByAuthority(ROLE_TEACHER)
			new UserRole(user: t, role: rol).save()
			ob.put("status", true)
			ob.put("message", "teacher created with id  "+t.teacherId)
		}catch(Exception e){

			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON




	}


	def addTeacherToDepartment() {
		
		
		
		Map ob = new HashMap();

		try{
			String dept_ids = params.dept_ids
			String[] arry_deptId = dept_ids.split(",")
			println dept_ids +" dept_ids"
			Long teacher_id = Long.parseLong(params.teacher_id)
			def teacher = Teacher.findByTeacherId(teacher_id)
			String depttags =teacher.tags
			for(String dept_id :arry_deptId){
				def dept = Department.get(Long.parseLong(dept_id))
				teacher.addToDepartments(dept)
				if(null == depttags ||"".equals(depttags.trim())){
					depttags = dept.dept_tags
				}else{
					depttags =depttags +","+ dept.dept_tags
				}

			}

			teacher.tags = depttags;
			teacher.save(flush:true);
			ob.put("status", true)
			ob.put("message", "teacher added to department ")
		}catch(Exception e){

			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON
	}

	def addSchoolClass() {
		Map ob = new HashMap();
		try{
			Long school_id = Long.parseLong(params.school_id)
			School school =  School.get(school_id);
			SchoolClass schoolClass = new SchoolClass()
			schoolClass.className = params.class_name
			schoolClass.school = school
			schoolClass.classTags = school.tags +",\""+schoolClass.className+"\""

			schoolClass.save(flush:true);
			ob.put("status", true)
			ob.put("message", "class added  having school anme  "+school.schoole_name)
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON



	}

	def addGradeClass() {
		Map ob = new HashMap();
		try{
		println "------------------------------------ ${params.class_name}"
		println "------------------------------------ ${params.section}"
			int class_name = Integer.parseInt(params.class_name)
			
			School school =  School.get(1)
	
			//SchoolClass schoolClass =  SchoolClass.get(class_id)
		//	School school =  School.get(school_id);
			SchoolClass schoolClass = new SchoolClass()
			schoolClass.className = params.class_name
			schoolClass.school = school
			schoolClass.classTags = school.tags +",\""+schoolClass.className+"\""
			schoolClass.save(flush:true);
			
			Grade grade = new Grade();
			grade.name=class_name
			grade.section = params.section
			
			if(params.classTeacherId){
			grade.classTeacherId=Long.parseLong(params.classTeacherId)
			}

			grade.gradetags = schoolClass.classTags +",\""+schoolClass.className+"-"+grade.section+"\""
			grade.schoolClass =schoolClass
			grade.save(flush:true);
			ob.put("status", true)
			ob.put("message", "section  added to class "+schoolClass.className )
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e)
		}
		render ob as JSON



	}
	
	def addGrade() {
		Map ob = new HashMap();
		try{
			Long class_id = Long.parseLong(params.class_id)

			SchoolClass schoolClass =  SchoolClass.get(class_id)
			Grade grade = new Grade();
			grade.section = params.section

			grade.gradetags = schoolClass.classTags +",\""+schoolClass.className+"-"+grade.section+"\""
			grade.schoolClass =schoolClass
				println "------------------------------------ [${device_token}]"
		println "------------------------------------ ${device_platform}"
			grade.save(flush:true);
			ob.put("status", true)
			ob.put("message", "section  added to class "+schoolClass.className )
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON



	}

	def addGuardian() {

		Map ob = new HashMap();
		try{

			Guardian guardain =	new Guardian()
			guardain.name = params.name
			guardain.username=params.username

			guardain.password= "123"
			guardain.educational_qualification= "MBA"
			guardain.designation= "Manager"
			guardain.profession= "Private Employee"
			guardain.emailId= params.emailId
			guardain.officeNumber= "04868699000"
			guardain.mobileNumber= "98470000"

			guardain.save(flush:true);
			ob.put("status", true)
			ob.put("message", "guardian added  ")
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON



	}

	def addStudent() {
		Map ob = new HashMap();
		try{
			def grade = Grade.get(Long.parseLong(params.grade_id))

			Student student =  new Student();
			student.grade = grade
			student.registerNumber = params.registerNumber
			student.studentName  = params.studentName
			student.gender= "male"
			student.dob = new Date();
			student.studentPhoto="photo.jpg"
			student.no_of_siblings=2
			student.present_guardian="Father"
			student.present_address =new Address(address: "Sample Address" , landmark: "Cochin" , place: "Kerala").save()
			student.save(flush:true);

			def	father = Guardian.findByUsername("guardian1@gmail.com")
			def mother = Guardian.findByUsername("guardian2@gmail.com")
			String father_tags  = father.tags;
			String mother_tags  = mother.tags;


			student.setAsFather( father )
			student.setAsMother( mother )
			println "------------------------------------ "+student.studentId
			if(father_tags == null){
				father_tags = grade.gradetags +",\"G\",\"S-"+student.studentId+"\""
			}else{
				father_tags = father.tags+",\"s-"+student.studentId+"\""
			}
			if(mother_tags == null){
				mother_tags = grade.gradetags +",\"G\",\"S-"+student.studentId+"\""
			}else{
				mother_tags = mother.tags+",\"s-"+student.studentId+"\""
			}
			println "------------------------------------ "+1
			Guardian.executeUpdate("Update Guardian set tags = '"+mother_tags+"' where username ='"+mother.username+"'")

			Guardian.executeUpdate("Update Guardian set tags = '"+father_tags+"' where username ='"+father.username+"'")
			println "------------------------------------ "+2



			ob.put("status", true)
			ob.put("message", "student added ")
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON

	}


	def addTeacherToSection() {

		Map ob = new HashMap();
		try{
			def grade = Grade.get(Long.parseLong(params.grade_id))
			def mathew = Teacher.findByTeacherId(Long.parseLong(params.teacher_id1))
			def sibi = Teacher.findByTeacherId(Long.parseLong(params.teacher_id2))
			grade.addToTeachers(mathew)

			grade.addToTeachers(sibi)
			grade.classTeacherId = mathew.id
			grade.classTeacherName = mathew.teacherName

			grade.save(flush:true)

			String mathew_tags  = mathew.tags;
			String sibi_tags  = sibi.tags;



			if(mathew_tags == null ){
				mathew_tags = grade.gradetags +",\"T-T\",\"T-"+mathew.teacherId+"\""
			}else{
				if(-1< mathew_tags.indexOf("T_T")){
					mathew_tags = mathew.tags+",\"T-"+mathew.teacherId+"\""
				}else{
					mathew_tags = grade.gradetags +",\"T-T\",\"T-"+mathew.teacherId+"\"" +mathew.tags
				}

			}

			if(sibi_tags == null){
				sibi_tags = grade.gradetags +",\"T-T\",\"T-"+sibi.teacherId+"\""
			}else{
				sibi_tags = sibi.tags+",\"T-"+sibi.teacherId+"\""
			}
			println "------------------------------------ "+1
			Teacher.executeUpdate("Update Teacher set tags = '"+mathew_tags+"' where username ='"+mathew.username+"'")

			Teacher.executeUpdate("Update Teacher set tags = '"+sibi_tags+"' where username ='"+sibi.username+"'")



			ob.put("status", true)
			ob.put("message", "teacher added to department ")
		}catch(Exception e){
			ob.put("status", false)
			ob.put("message", "failed due to "+e.getMessage())
		}
		render ob as JSON


	}

	def registerForpush() {
		//will get the username from spring security but for now i am not using
		String username = params.username
		String device_platform = params.platform
		String device_token = params.token;
		User user = User.findByUsername(username)
		String userTags = user.tags
		User.executeUpdate("Update User set deviceToken = '"+device_token+"',tagRegister=true,platform='"+device_platform+"' where username ='"+username+"'")

		def rest = new RestBuilder()
		def resp = rest.put("https://api.pushbots.com/deviceToken"){
			header 'x-pushbots-appid', '550e9e371d0ab1de488b4569'
			header 'x-pushbots-secret', 'e68461d7755b0d3733b4b36717aea77d'
			json {
				token ="${device_token}"
				platform ="${device_platform}"
				alias ="${username}"
				tag = "${userTags}"
				payload="JSON"

			}


		}
		println "------------------------------------ [${userTags}]"
		println "------------------------------------ ${username}"
		println resp.json as JSON

		Map ob = new HashMap();
		ob.put("status", true)
		ob.put("message", "success")

		render ob as JSON
	}

	def addHomeWork() {
		//will get the username from spring security but for now i am not using
		String username = params.username
		String message = params.message
		Long class_id = Long.parseLong(params.class_id);
		Long section_id
		if(params.class_id != null){
			section_id = Long.parseLong(params.section_id)
		}

		boolean isForAllStudent = params.forAllStudent

		// for tesing i ma taking for all class parents
		SchoolClass schoolClass =  SchoolClass.get(class_id)

		String tags = schoolClass.classTags+",T"
		def rest = new RestBuilder()
		def resp = rest.post("https://api.pushbots.com/push/all"){
			header 'x-pushbots-appid', '550e9e371d0ab1de488b4569'
			header 'x-pushbots-secret', 'e68461d7755b0d3733b4b36717aea77d'
			json {
				token ="${device_token}"
				platform =["0", "1"]
				alias = "${username}"
				tag = ["${userTags}"]
				sound = "ding"
				badge = "187192"
				msg = "${message}"
				except_tags =[]
				active =[]
				except_active =[]
				alias= ""

				payload="JSON"



			}


		}
		println resp.json as JSON

		Map ob = new HashMap();
		ob.put("status", true)
		ob.put("message", "success")

		render ob as JSON
	}

	def getTest() {
		String s = "\"hi\""
		println s
		Map ob = new HashMap();
		ob.put("status", true)
		ob.put("message", "success")

		render ob as JSON
	}



	def teacherGradesSubjects()
	{
		try{

			Grade grade
			Subject subject
			Teacher t
			t = Teacher.get(Integer.parseInt(params.teacherId))
			params.list.each{
				grade = Grade.get(Integer.parseInt(it.gradeId))
				subject = Subject.get(Integer.parseInt(it.subjectId))
				t.addToGradeSubject(grade,subject)

			}

		}
		catch(Exception e)
		{

			render e as JSON
		}
	}


	def assignDepartment()
	{
	   def output = [:]
	   try{

		   Department dept = Department.findOrSaveWhere(dept_name: params.departmentName )
		   Teacher teacher
		   if(dept.teachers) { dept.teachers.clear() }
		   params.teachers.each{
			  teacher = Teacher.get(Integer.parseInt(it))
			  dept.addToTeachers(teacher)


		   }
		   if(dept.save(flush: true))
		   {
			   output['status'] = 'success'
			   output['message'] = 'Details sucessfully added'
			   render output as JSON
		   }

	   }
	   catch (Exception e)
	   {
		render e

	   }

	}




	 def getDeptTeachers()
	 {
		 def result = [:]
		 try{

			 Department dept = Department.findByDept_name(params.departmentName)
			 if(dept)
			 {
				 result['status'] = true
				 result['teachers'] = dept.teachers.collect(){ [teacherId:it.id ,teacherName : it.teacherName ] }

			 }
			 else
			 {
				 result['status'] = false
				 result['teachers'] = "No Teachers"
			 }
			 render result as JSON
		 }
		 catch (Exception e)
		 {
				render e as JSON
		 }


	 }


	 def setClassTeacher()
	  {

		  def result = [:]
		  try {

			   Grade g = Grade.get(Integer.parseInt(params.gradeId))
			   Teacher t = Teacher.get(Integer.parseInt(params.teacherId))

			   Grade.withNewSession {
				   Grade.executeUpdate("update Grade g set g.classTeacherId = :teacher where  g.gradeId = :gid ",[teacher : t.id , gid:g.gradeId])
				   t.addToGrades(g).save()
				   g.addToTeachers(t).save()
				     }
			

					  result['status'] = 'success'
					  result['data'] = Grade.get(Integer.parseInt(params.gradeId))
					  render result as JSON

			  }
		  catch(Exception e)
		  {
			  render e as JSON
		  }

		  }



	   def getTeacherTimeTableList()
		  {

			  def output = new ArrayList()
			  def teacherTT = [:]
			  def timetables = [:]
			  Teacher teacher
			  try{
				  def days = TimeTable.executeQuery("select distinct a.day from TimeTable a ")

				  Teacher.findAll().each {

					  teacher = it
					  teacherTT['teacherId'] = it.id.toString()
					  teacherTT['teacherName'] = it.teacherName

					  days.each {
						  timetables[it] = TimeTable.findAllByTeacherAndDay(teacher,it)
					  }
					  teacherTT['timetables'] = timetables
					  output.push(teacherTT)
					  teacherTT = [:]
					  timetables = [:]
				  }
				render output as JSON



			  }
			  catch(Exception e)
			  {
				  render e as JSON
			  }


		  }




	   def classSubjectTeacherList()
	   {
		   def output = [:]
		   def list = new ArrayList()
		   def subjectTeacherList
		   try{
			   def grades = GradeTeacherSubject.executeQuery("select distinct g.grade  from GradeTeacherSubject g ")

			   grades.each {


				 output['gradeId'] = it.gradeId.toString()
				 output['gradeName'] = it.name.toString()
				 output['section'] = it.section


									 subjectTeacherList  = GradeTeacherSubject.findAllByGrade(it)
									 output['subjectTeacherList'] = subjectTeacherList.collect(){ [
										 subject :[subjectId: it.subject.subjectId.toString() ,
												   subjectName: it.subject.subjectName ,
										 ],
										 teacher: [teacherId: it.teacher.id.toString() ,
												   teacherName: it.teacher.teacherName ,
												   teacherEmailId: it.teacher?.teacherEmailId ,
												   teacherPhoto: it.teacher?.teacherPhoto

										 ]
											 ]

									 }



				   list.push(output)
				   output = [:]



			   }
			   render list as JSON
		   }
		   catch(Exception e)
		   {
				  render e

		   }

	   }

	   def getTeacherWeekTimetableVer1()
	   {
   
		   try {
   
			   user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
			   Teacher teacher = Teacher.findByUsername(user.username)
   
			   def output = [:]
			   output['teacherId'] = teacher.id.toString()
			   output['teacherName'] = teacher.teacherName
   
   
			   def teacherTT = [:]
   
			   def days = TimeTable.executeQuery("select distinct a.day from TimeTable a ")
			   JSON.use('TeachergetTimeTable')
					   {
						   days.each {
							   teacherTT[it] = TimeTable.findAllByTeacherAndDay(teacher,it)
						   }
						   output['timeTable'] = teacherTT
						   render output as JSON
					   }
   
		   }
		   catch (Exception e)
		   {
			   render e
		   }
	   }
   
	   


	 

	   def teacherExams()
	   {
		   def output = [:]
		   try {

			   Long id
			   if(params.teacherId) { id = Long.parseLong(params.teacherId) }
			   else {
				   User user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
				   id = user.id
			   }
			   Teacher t = Teacher.findById(id)

			   output['teacherId'] = id.toString()
			   output['teacherName'] = t.teacherName
			   output['teacherEmailId'] = t.teacherEmailId
			   output['teacherPhoto'] = t.teacherPhoto
			   output['username'] = t.username
			   output['teacherExams']  = ExamSchedule.findAllByTeacher(t).collect() { [examId:it.exam?.examId.toString() , examName : it.exam?.examName , examType:it.exam?.examType , class:it.exam?.schoolclass?.className.toString() ,
																					   grade:[gradeId : it.exam?.grade?.gradeId.toString() , gradeName: it.exam?.grade?.name.toString() , section:it.exam?.grade?.section  ] ,
																					   'subjectName':  it.subject.subjectName,
																					   'subjectSyllabus':it.subjectSyllabus.syllabus,
																					   'teacherName':it.teacher.teacherName,
																					   'examStartTime':it.startTime? exSchedule.startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"):'date not',
											  
																					   'examEndTime':it.endTime? exSchedule.startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"):'date not' ]
											  
											  
																						}
			   render output as JSON


		   }
		   catch (NullPointerException e)
		   {
				   output['status'] = 'error'
				   output['message'] = 'Teacher id '+ params.teacherId +' does not exist. Check teacher id.'
			   render output as JSON

		   }
		   catch (Exception e)
		   {
			   render e
		   }

	   }

 
	   def teacherEvaluationUpload()
	   {

		   try{
			   def file = request.getFile('file')
			   if(file.empty) {
				   System.out.println("File cannot be empty")
			   } else {
				   def values =  JSON.parse(params.data)
				   Integer teacherId = Integer.parseInt(values.teacherId)
				   Teacher teacher = Teacher.get(teacherId)
				   Integer year = Integer.parseInt(values.year)
				   def documentInstance = PerfomanceEvaluation.findOrCreateWhere(teacher: teacher,year: year);
				   documentInstance.filename = file.originalFilename
				   documentInstance.filedata = file.getBytes()
				   documentInstance.teacher = teacher
				   documentInstance.year = year


				   if(documentInstance.save()){
				   render "Teacher evaluation record saved successfully"
				   }else
				   {
					   render "Some error has been occured. Please try again"
				   }

			   }

		   }
		   catch(Exception e)
		   {
			   render e as JSON
		   }
	   }




	   def gradeSubjectList()
		  {
			  try{

				   Teacher teacher = Teacher.get(Integer.parseInt(params.teacherId));
				   Grade grade = Grade.findByNameAndSection(Integer.parseInt(params.grade) , params.section);
				   Subject subject
				   params.subjects.each{

						subject = Subject.findBySubjectId(Integer.parseInt(it))
						teacher.addToGradeSubject(grade,subject)
				   }

				  def result = [:]
				  result['status'] = 'success'
				  render result as JSON


			  }
			  catch (Exception e)
			  {
				  render e
			  }


		  }

		  



}



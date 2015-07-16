package ghumover2

import grails.converters.JSON

import java.text.SimpleDateFormat

class ExamDetailsController {

	def springSecurityService
	 def addExams()
    {
            try{

               Exam exam;
               Grade grade;
               SchoolClass schoolClass;
               Subject subject;
               ExamSyllabus syllabus;
               ExamSchedule schedule;
                def result = [:]

                if(params.exam['grade'])
                {

                    String gid =  params.exam['grade'];
                    Integer gradeId = Integer.parseInt(gid)
                    grade = Grade.get(gradeId);
                    exam = new Exam(examName: params.exam['examName'] , examType: params.exam['examType'] , grade: grade ).save()





                }
                else
                {

                    String sid =  params.exam['schoolclass'];
                    Integer scId = Integer.parseInt(sid)
                    schoolClass = SchoolClass.get(scId);
                    exam = new Exam(examName: params.exam['examName'] , examType: params.exam['examType'] ,schoolClass:schoolClass ).save()

                }
               def x  = new ArrayList();
                int subId;
               params.schedule.each{
                subId = Integer.parseInt(it['subjectId']);
                subject = Subject.get(subId)
                syllabus = new ExamSyllabus(exam:exam ,  subject: subject , syllabus:it.syllabus).save();
                schedule = new ExamSchedule(exam:exam , subjectSyllabus: syllabus , subject: subject ,startTime: it.startTime , endTime: it.endTime).save()
                x.push(schedule)
               }
              if(x)
              {
                  result['status'] = 'success'
                  result['data'] = exam
                  render result as JSON
              }

            }
            catch(Exception e)
            {

            }

    }


	def newExamSyllabus()
	{
		def output = [:]
		try{

			int examId = Integer.parseInt(params.examId)
			int subjectId = Integer.parseInt((params.subjectId))
			String syllabus = params.syllabus
			Exam e = Exam.findByExamId(examId)
			Subject s = Subject.findBySubjectId(subjectId)
			def saveSyllabus = new ExamSyllabus(exam: e , subject:s ,syllabus: syllabus).save(flush: true)
			if(saveSyllabus)
			 {
				 output['satatus'] = 'success'
				 output['message'] = 'Exam Syllabus saved successfully'
				 output['data'] = saveSyllabus.collect{ ['examId':it.exam?.examId , examName: it.exam?.examName , "Subject" : it.subject.subjectName ,"syllabus" : params.syllabus ]  }
				 render output as JSON
			 }
			render params



		}
		catch(Exception e)
		{
			render e as JSON

		}

	}

	def newExamSchedule()
	{

		def output = [:]
		try{

			int examId = Integer.parseInt(params.examId)
			int syllabusId = Integer.parseInt(params.syllabusId)
			int subjectId = Integer.parseInt(params.subjectId)
			int teacherId = Integer.parseInt(params.teacherId)
			SimpleDateFormat ft =  new SimpleDateFormat ("dd-MM-yyyy hh:mm");

			Date startTime = ft.parse(params.startTime);

			Date endTime = ft.parse(params.endTime);



			Exam e = Exam.findByExamId(examId)
			ExamSyllabus es = ExamSyllabus.findById(syllabusId)
			Subject sub = Subject.findBySubjectId(subjectId)
			Teacher t = Teacher.findById(teacherId)

			def saveSchedule = new ExamSchedule(exam: e , subjectSyllabus: es , subject: sub , teacher: t , startTime: startTime, endTime : endTime ).save(flush: true)
			if(saveSchedule)
			{
				output['satatus'] = 'success'
				output['message'] = 'Exam Schedule saved successfully'
				output['data'] = saveSchedule.collect{ ['examId':it.exam?.examId , examName: it.exam?.examName , "Subject" : it.subject.subjectName ,"teacher" : it.teacher?.teacherName ,"examDate": it.startTime.format('dd-MM-yyyy')  ,  "startTime" :it.startTime.format("KK:mm a")  , "endTime" :  it.endTime.format("KK:mm a") ]  }
				render output as JSON
			}





		}
		catch(Exception e)
		{
			   render e as JSON
		}
	}

	def newExamResult()

	{
		def output = [:]
		try{
			int examId = Integer.parseInt(params.examId)
			int studentId =  Integer.parseInt(params.studentId)
			int marks = Integer.parseInt(params.marks)
			int maxMarks = Integer.parseInt(params.maxMarks)
			int subjectId = Integer.parseInt(params.subjectId)

			Exam exam = Exam.findByExamId(examId)
			Student student = Student.findByStudentId(studentId)
			Subject sub = Subject.findBySubjectId(subjectId)


			def saveResult = new ExamResult(exam: exam , student: student , subject: sub , marks: marks , maxMarks: maxMarks )

			if(saveResult)
			{
				output['status'] = 'success'
				output['message'] = 'Result stored successfully'
				output['data'] = saveResult.collect(){ [ student: it.student?.studentName , exam : it.exam?.examName , maxMarks: it.maxMarks , marks: it.marks]}
			}

			render output as JSON





		}
		catch (Exception e)
		{
			 render e as JSON
		}
	}


	def studentResult()
	{

		try{

			 Long studentId = Long.parseLong(params.studentId)
			 Student student = Student.get(studentId)
			 SchoolClass sc = SchoolClass.findByClassName(student.grade.name)

			 def exams = Exam.findAllByGradeOrSchoolclass(student.grade,sc)
			 def res = exams.collect(){
				 [
						 studentId: studentId.toString() ,
						 studentName: student.studentName ,
						 examId : it.examId.toString() ,
						 examName : it.examName ,
						 examType : it.examType ,
						 class : it.schoolclass?.className.toString() ,
						 grade : [gradeId : it.grade?.gradeId.toString() , gradeName: it.grade?.name , section : it.grade?.section] ,
						 examSchedule : it.examSubjectSchedule.collect()    { ExamSchedule es -> [ subject:[ subjectId:  es.subject?.subjectId.toString() ,
																											subjectName: es.subject?.subjectName ] ,
																								  syllabus : [ id:es.subjectSyllabus?.id.toString() , syllabus: es.subjectSyllabus.syllabus] ,
																								 examDate : es.startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") ,
																								 startTime : es.startTime.format("KK:mm a") ,
																								 endTime: es.endTime.format("KK:mm a")]
						 } ,
						 result : (it.results.findAll { it.student.studentId == studentId  }) ? (it.results.findAll { it.student.studentId == studentId  }).collect() { [ subjectId : it.subject?.subjectId.toString() , subjectName: it.subject?.subjectName , marks: it.marks.toString() , maxMarks: it.maxMarks.toString(),grade:"A",status:"Pass"] }:[],
						 totalMarks:"270",
						 totalMaxMark:"300",
						 overallGrade:"A",
						 overallStatus:"Pass"
						 
						 
				 ]
				 
				 
			 }

			def output = [:]
			output['exam'] = res
			render output as JSON


		   }
		catch(Exception e)
		{
			render e as JSON

		}

	}

	def teacherExamsSchedule()
	{
		
				   def output = [:]
				   try {
					   def exams = [:]
					   Long id
					   if(params.teacherId) { id = Long.parseLong(params.teacherId) }
					   else {
						   User user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
						   id = user.id
					   }
					   Teacher t = Teacher.findById(id)
					//   output['teacherId'] = id.toString()
					  // output['teacherName'] = t.teacherName
					   //output['teacherEmailId'] = t.teacherEmailId
					   //output['teacherPhoto'] = t.teacherPhoto
					   //output['username'] = t.username
		
		
					   def teacherGrades =  GradeTeacherSubject.executeQuery("select distinct g.grade from GradeTeacherSubject  as g where g.teacher = ?",t)
					   if(null!=teacherGrades && teacherGrades.size()>0){
					   def sc_list = teacherGrades.collect(){ it.name }.unique()
		
					   def schoolclasses = SchoolClass.findAll("from SchoolClass as s where s.className in (:s_list) ", [s_list:sc_list])
					   def examList = Exam.findAll("from Exam as e where e.grade in (:list) or e.schoolclass in (:c_list)" ,[list:teacherGrades , c_list:schoolclasses])
					   def curExam
					   def schedule
					   def listOfExams = new ArrayList()
					   def subjects
					   def gradeExams
					   examList.each {
						   curExam = it
						   if(it.grade)
							{
								subjects = t.getSubjectsInGrade(it.grade).collect(){ it.subject }
								gradeExams = ExamSchedule.findAll("from ExamSchedule  as e where e.exam = :exm and e.subject in (:sub)",[exm:curExam , sub:subjects])
								if(gradeExams.size() > 0)
								{
		
									exams['examId'] = curExam?.examId.toString()
									exams['examName'] = curExam?.examName
									exams['examType'] = curExam?.examType
									exams['class'] =   (curExam.schoolclass) ? curExam?.schoolclass?.className.toString() : curExam.grade.name.toString()
									exams['grade'] = [gradeId : it.grade?.gradeId.toString() , gradeName: it.grade?.name , section : it.grade?.section]
									
									exams['examSchedule'] = gradeExams.collect(){ [ subject:[ subjectId:  it.subject?.subjectId.toString() ,
																											subjectName: it.subject?.subjectName ]
									 ,  examDate : it.startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") ,
																								 startTime : it.startTime.format("KK:mm a") ,
																								 endTime: it.endTime.format("KK:mm a") ] }
											listOfExams.push(exams)
									exams = [:]
								}
							}
						   else
						   {
		
							   exams['examId'] = curExam?.examId.toString()
							   exams['examName'] = curExam?.examName
							   exams['examType'] = curExam?.examType
							   exams['class'] =   (curExam.schoolclass) ? curExam?.schoolclass?.className.toString() : curExam.grade.name.toString()
							   exams['section'] = (curExam.grade)? curExam.grade.section : "NA"
							   exams['examSchedule'] = ExamSchedule.findAll("from ExamSchedule  as e where e.exam = :exm ",[exm:curExam]).collect(){ [subjectName : it.subject.subjectName ,  examDate : it.startTime.format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") ,
																								 startTime : it.startTime.format("KK:mm a") ,
																								 endTime: it.endTime.format("KK:mm a") ] }
							   listOfExams.push(exams)
							   exams = [:]
		
		
						   }
		
		
					   }
					   output['exam'] = listOfExams
					  render output as JSON
					   
					  }else{
							output['status'] = 'error'
						  output['message'] = 'Teacher id '+ params.teacherId +' does not exist. Check teacher id.'
						  render output as JSON
							  }
		
		
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
}




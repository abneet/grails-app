package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class Exam {

	Long examId
	String examName
	String examType
	Grade grade
	SchoolClass schoolclass



	static hasMany = [examSubjectSchedule:ExamSchedule , results:ExamResult , examSyllabus:ExamSyllabus ]


	static mapping = {
		id generator: 'increment',name: 'examId'
	}


	static constraints = {

		grade(nullable:true)
		schoolclass(nullable: true)
	

	}
	
	
	
	
	
}
package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class ExamResult {
    Long resultId
    static belongsTo = [exam:Exam]
    Student student
    Subject subject
    int marks
	String grade	
	String status

    int maxMarks

    static constraints = {
		 grade(nullable: true)
		 status(nullable: true)

    }
    static mapping = {
        id generator: 'increment',name: 'resultId'
    }

}
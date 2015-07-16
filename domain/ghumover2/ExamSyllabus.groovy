package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class ExamSyllabus {

    static belongsTo = [exam:Exam]
    Subject subject
    String syllabus

    static constraints = {
        exam(nullable: true)
    }
}

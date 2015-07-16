package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class GradeTeacherSubject {

    Grade grade
    Teacher teacher
    Subject subject

    static constraints = {

        grade unique: ['teacher' , 'subject']
    }
}

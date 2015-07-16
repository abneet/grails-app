package ghumover2

import grails.converters.JSON
import grails.rest.RestfulController


class StudentController extends RestfulController  {
    static responseFormats = ['json', 'xml']
    StudentController() {
        super(Student)
    }


    def getStudentsOfClass()
    {

        def output = [:]
        try {

            Grade grade = Grade.findByNameAndSection(Integer.parseInt(params.grade),params.section)
            JSON.use('studentDetail')
                    {
                            output['total_no_of_students'] = grade.students.size()
                            output['students'] = grade.students
                            render output as JSON
                    }

        }
        catch (Exception e)
        {

            render e

        }

    }






}
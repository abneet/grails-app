package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class Department {
    Long dept_id;
    String dept_name;
    String dept_tags;
    Long school_id
    static hasMany = [teachers:Teacher]
    static mapping = {
        id generator: 'increment',name: 'dept_id'


    }

    def afterInsert = {
        dept_id = this.dept_id;

        String tagname = "\""+(this.dept_name).replace(' ','')+"-"+dept_id+"\""
        println tagname
        dept_tags = tagname;
        Department.executeUpdate("Update Department set dept_tags = '"+tagname+"' where dept_id ="+dept_id)
    }

    static constraints = {
        dept_tags(nullable: true)
        school_id(nullable: true)
    }
}

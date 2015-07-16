package ghumover2

class School {
    Long school_id;
    String schoole_name;

    String tags;
    String address

    String landmark
    String landline;

    static hasMany = [schoolClasses :SchoolClass]

    static mapping = {
        id generator: 'increment',name: 'school_id'


    }
    static constraints = {

        tags(nullable: true)

    }

    def afterInsert = {
        school_id = this.school_id;
        tags = this.schoole_name
        String tagname = "\""+tags.replace(' ','')+"-"+school_id+"\""
        println tagname
        tags = tagname;
        //School.executeUpdate("Update School set tags = '"+tagname+"' where school_id ="+school_id)
    }
}
package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class SchoolClass {
	
	Long classId
	int className
	String classTags
	static belongsTo =
	[school: School]
	static hasMany = [grades:Grade]
	
	static mapping = {
		id generator: 'increment',name: 'classId'
		
	}
    static constraints = {
		classTags(nullable: true)
		school(nullable: true)


    }
	
	def afterInsert = {
		classId = this.classId;
		
	}
}

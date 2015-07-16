package ghumover2
import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class Subject {
	
	Long subjectId
	String subjectName
	static mapping = {
		id generator: 'increment',name: 'subjectId'
	}
	static constraints = {

	}

}

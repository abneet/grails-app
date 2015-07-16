package ghumover2

import grails.rest.Resource
import org.grails.databinding.BindingFormat

import java.text.SimpleDateFormat

@Resource(formats=['json', 'xml'])
class Homework
{
	Long homeworkId
	Grade grade
	String subject
	@BindingFormat("dd-MM-yyyy")
	Date dueDate
	String homework
	@BindingFormat("dd-MM-yyyy")
	Date dateCreated
	Student student
	String message
	String gradeFlag
	static namedQueries = {
		todaysPosts {
		   def now = new Date().clearTime()
		   between('dateCreated', now, now+1)
		 
		}
	}
	static constraints = {
		student(nullable:true)
		grade(nullable:true)
		homeworkId(nullable: true)
		grade(nullable: true)
	
		message(nullable: true)
		



		}

	static mapping = {
						id generator: 'increment',name: 'homeworkId'
						dateCreated  sqlType: "DATE"
						dueDate sqlType: "DATE"
					   }




}

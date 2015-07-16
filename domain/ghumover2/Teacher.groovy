package ghumover2
import grails.rest.Resource

import org.grails.databinding.BindingFormat

import javax.persistence.Transient

@Resource(formats=['json', 'xml'])
class Teacher  extends User  {

	 static belongsTo = [Grade , Department]
	 static hasMany = [grades:Grade,subject:Subject , timetables:TimeTable,departments:Department]

	 Long teacherId
	 String teacherName
	 String teacherPhoto
	 String teacherEmailId
	 String phoneNo
	 String empCode
	 @BindingFormat('dd-MM-yyyy')
	 Date doj
	 String qualification
	 String tAddr
	 @BindingFormat('dd-MM-yyyy')
	 Date DOB
	 String post
	 
	
	 static mapping = {
		  id generator: 'increment',name: 'teacherId'
			  cache true
			
			  
		  
		  
	}
	 
 def afterInsert()
	 {
	 this.name = this.teacherName
 }

 def afterUpdate()
 {
	 this.name = this.teacherName
 }

	static constraints = {
		
		phoneNo(nullable: true)
		teacherEmailId(nullable: true)
		teacherPhoto(nullable: true)
		empCode(nullable:true)
		doj(nullable:true)
		qualification(nullable:true)
		tAddr(nullable:true)
		post(nullable:true)
		DOB(nullable:true)
	}


	void addToGradeSubject(Grade grade , Subject subject)
    {



			new GradeTeacherSubject(grade: grade, teacher: this , subject:subject).save()
			this.addToGrades(grade).save()
			grade.addToTeachers(this).save()
			this.addToSubject(subject).save()

    }
	void removeFromGradeSubject(Grade grade , Subject subject)
	{
		GradeTeacherSubject.findByGradeAndTeacherAndSubject(grade,this,subject).delete()
	}

	def getSubjectsInGrade(grade)
	{
		return GradeTeacherSubject.findAllByGradeAndTeacher(grade,this)
	}


	def getAllGradesAndSubjects()
	{
		return GradeTeacherSubject.findAll("from GradeTeacherSubject as g where g.teacher = ? ",[this])
	}



}

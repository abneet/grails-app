package ghumover2
import grails.rest.Resource

import org.grails.databinding.BindingFormat

import javax.persistence.Transient

import java.text.SimpleDateFormat;

@Resource(formats=['json', 'xml'])
class Student  {

	Long studentId
	String registerNumber
	String studentName
	String gender
	Address present_address
	int no_of_siblings
	@BindingFormat('dd-mm-yyyy')
	Date dob
	String studentPhoto
	String present_guardian
	String modeOfTransport
	String bloodGroup
	String medicalCondition
	String feeType
	@BindingFormat('dd-mm-yyyy')
	Date admDate
	String category
	String religion
	//String  medicalDetails
	static belongsTo = [grade : Grade]

	static hasMany = [siblings:Sibling]

	static mapping = { id generator: 'increment',name: 'studentId'
		dob sqlType: "DATE"
		
			cache true
		
	}



	static constraints = {

		gender(nullable: true)
		registerNumber(nullable:true)
		grade(nullable:true)
		present_address(nullable: true)
		no_of_siblings(nullable :true)
		dob(nullable: true)
		studentPhoto(nullable: true)
		 present_guardian(nullable: true)
		  modeOfTransport(nullable: true)
		  bloodGroup(nullable: true)
		 medicalCondition (nullable:true)
		 feeType(nullable:true)
		 admDate(nullable:true)
		 category(nullable:true)
		 religion(nullable:true)
	}

	void setAsFather(Guardian father)
	{
		new GuardianChildren(guardian:father, student:this , guardianType:"father").save()
	}
	void setAsMother(Guardian mother)
	{
		new GuardianChildren(guardian: mother , student:this , guardianType:"mother").save()
	}
	void setAsLocalGuardian(Guardian local_guardian)
	{

		new GuardianChildren(guardian: local_guardian , student:this , guardianType:"local_guardian").save()

	}



	@Transient
		List<String>  getTags() {
          tags=[]
		 Guardian motherTags=GuardianChildren.findByStudentAndGuardianType(this,"mother")?.guardian
           tags<<motherTags.tags
		   Guardian fatherTags=GuardianChildren.findByStudentAndGuardianType(this,"father")?.guardian
		   tags<<fatherTags.tags
		   return tags
	}
	

	@Transient
	Guardian getMother() {

		return GuardianChildren.findByStudentAndGuardianType(this,"mother")?.guardian

	}

	@Transient
	Guardian getFather() {
		return GuardianChildren.findByStudentAndGuardianType(this,"father")?.guardian
	}

	@Transient
	Guardian getLocalGuardian() {

		return GuardianChildren.findByStudentAndGuardianType(this,"local_guardian")?.guardian

	}
	@Transient
	List<Student> getParents() {
		return  GuardianChildren.findAllByStudent(this).guardian
	}


	def getAttendance(String from_date , String to_date)
	{

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		Date date_from = formatter.parse(from_date);
		Date date_upto = formatter.parse(to_date)

        def allAttendance =   Attendance.findAllByDateBetween(date_from ,date_upto)
        def studentAbsense = new ArrayList()
		allAttendance.each { attendence ->

			if(attendence.absentees.studentId.contains(this.studentId))
			 {
				 studentAbsense << attendence
			 }


		}
		return studentAbsense

	}




	def getAge()
	{

		return "age"
	}



}

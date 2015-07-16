package ghumover2

import org.grails.databinding.BindingFormat

class Attendance {

	Long attendanceId
	@BindingFormat('dd-MM-yyyy')
	Date date
	static belongsTo = [grade:Grade]
	static hasMany = [absentees:Student]
	Boolean all_present



	static mapping = {

		date  sqlType: "DATE"
		id generator: 'increment',name:'attendanceId'
	}

	static constraints = {
		all_present(nullable: true)
		grade(unique: 'date')
	}
}

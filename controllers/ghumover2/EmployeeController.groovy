package ghumover2

class EmployeeController {
	
	
    def saveEmployee() {
		
		def emp1 = new Emplyee()
		emp1.firstName = 'test1'
		emp1.lastName = 'test1'
		emp1.save(flush: true)
		println "hi"+ params.id
		//render (view:'index.gsp')
	}
}

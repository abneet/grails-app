package ghumover2

import grails.converters.JSON
import grails.rest.RestfulController
import groovy.json.JsonSlurper



class StudentDetailsController extends RestfulController  {
    static responseFormats = ['json', 'xml']
	def springSecurityService
    StudentDetailsController() {
        super(Student)
    }

	//static allowedMethods = [forgetPassword: "POST"]
	private static final String ROLE_TEACHER = 'ROLE_TEACHER'
	private static final String ROLE_PARENT = 'ROLE_PARENT'
	private static final String ROLE_ADMIN = 'ROLE_ADMIN'
	
	def forgetPassowrd(){
		String emailId = params.emailId
	//	String newPassword = params.password_new
		println "test this {{emailId}}"
		def message
		User user=User.findByUsername(emailId)
		if(user){
			user.password="1234"
			user.save()
			//sendMail
			message="password sent to registered mail id"
		}else{
		
		message="email id is not valid"
		}
		
		render message as JSON
	}
	
	
	
	
	def updatePassword() {
		def result = [:]
		
		def message ="error"
		
		println "test this {{params.password}}"
		
		String password = params.password
		String newPassword = params.password_new
		String newPassword2 = params.password_new2
		
		if (!password || !newPassword || !newPassword2 || newPassword != newPassword2) {
			message = 'Please enter your current password and a valid new password'
		
			result['status'] = 'error'
			result['message']=message
		    render result as JSON
		   
		}
		
	
	User user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
		//if (!springSecurityService.passwordEncoder.isPasswordValid(user.password, password, null /*salt*/)) {
		//   message = 'Current password is incorrect'
		   
		//   render message
	//	}
	 
	//	if (passwordEncoder.isPasswordValid(user.password, newPassword, null /*salt*/)) {
		//   message = 'Please choose a different password from your current one'
		  // render message
	//	}
	 
		user.password = newPassword
		user.passwordExpired = false
		user.save() // if you have password constraints check them here
	     message="password changed successfully"
		result['status'] = 'success'
			result['message']=message
		   render result as JSON
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


  def studentSave()
    {
        try{

               Guardian father,mother,local

               if(params.father)
               {
                    params.father['username'] = params.father['emailId'];
                    params.father['password'] = '123';
                    
                    father = new Guardian(params.father).save()
               }
            else
               {
                   father = new Guardian(name: "default_name" , username: params.student['registerNumber']+"_father" , password: 123 ).save();
               }
              if(params.mother)
              {
                   params.mother['username'] = params.mother['emailId'];
                   params.mother['password'] = '123';
                   mother = new Guardian(params.mother).save()

              }
            else
              {
                  mother = new Guardian(name: "default_name" ,username: params.student['registerNumber']+"_mother" , password: 123 ).save();
              }
             if(params.local_guardian)
             {
                params.local_guardian['username'] = params.local_guardian['emailId']
                params.local_guardian['password'] = '123';
                local = new Guardian(params.local_guardian).save()
             }
            else
             {
                local = new Guardian(name: "default_name" ,username: params.student['registerNumber']+"_local_guardian" , password: 123 ).save();
             }

                Grade grd = Grade.findByGradeId(Integer.parseInt(params.student['gradeId']));

               Student s = new Student(params.student)
               Address address = new Address(params.address).save()
               s.present_address = address
               s.grade = grd;
               s.save()


                s.setAsFather(father)
                s.setAsMother(mother)
                s.setAsLocalGuardian(local)




                def output = [:]
                output['status'] = 'success'
                output['data'] =  s
                render output as JSON



        }
        catch (Exception e)
        {
              render e as JSON

        }

    } 






}
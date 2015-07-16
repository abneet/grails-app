package ghumover2
import grails.rest.Resource
import javax.persistence.Transient;
@Resource(formats=['json', 'xml'])
class Guardian extends User
{

   //String name

    String educational_qualification
    String profession
    String designation
    String mobileNumber
    String emailId
    String officeNumber

	static mapping={
		cache true
	}


    static constraints = {
        educational_qualification(nullable: true)
        profession(nullable: true)
        designation(nullable: true)
        mobileNumber(nullable: true)
        emailId(nullable: true)
        officeNumber(nullable: true)
    }


    void addToChildrens(Student child) {
        new GuardianChildren(guardian:this, student:child , guardianType:"father").save()
    }

    @Transient
    List<Student> getChildren() {
        return  GuardianChildren.findAllByGuardian(this).student
    }
}
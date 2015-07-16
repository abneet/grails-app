package ghumover2

import grails.rest.Resource

@Resource(formats=['json', 'xml'])
class Notice {
	def springSecurityService
	Long noticeId
	String topic
	String message
	String senderName
	String designation
	Date date
	String day
	String salutation
	String createBy
	String signoffWord
	
    static constraints = {
		topic(nullable: true)
		message(nullable: true)
		senderName(nullable: true)
		designation(nullable: true)
		date(nullable: true)
		day(nullable: true)
		salutation(nullable: true)
		createBy(nullable: true)
		signoffWord(nullable: true)


    }
	def beforeInsert() {
		date = new Date()
		Calendar calendar = Calendar.getInstance();
		int d = calendar.get(Calendar.DAY_OF_WEEK);
		def days = ["", "Sunday", "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"]
		day = days[d];
		User user = springSecurityService.loadCurrentUser()
		senderName = user.username
		createBy = user.username



	}
	static mapping = { id generator: 'increment',name: 'noticeId'}



}

package ghumover2

import grails.converters.JSON



class NoticeDetailsController {

    def index() { }
	def getnotices(){
		
		def  noticeList =[]
		
		def ntc=new Notice()
		ntc.setNoticeId(1)
		ntc.setDate(new Date())
		ntc.setDay("Monday") 
		ntc.setDesignation("Class Teacher 	")
		ntc.setMessage("Welcome to a new school year. I am so excited to have your child in my class this year. I would like to get to know a little more about your child and what I can do to help your child have a meaningful year. Please answer the following questions and return to me as soon as possible. Your response is very important to me . Together we can make a team. I am looking forward to a great year. ")
		ntc.setSalutation("Dear Parents ")
		ntc.setSignoffWord("Yours Sincerly")
		ntc.setTopic("Notice from class Teacher")
		ntc.setSenderName(" ")
		ntc.setCreateBy("Principle")
	

		noticeList.add(ntc)
		
		
		
		def output = [:]
		output['Notices'] = noticeList.collect{[
			'noticeId': it.noticeId.toString(),
				  'topic':it.getTopic(),
				  'salutation':it.getSalutation(),
				  'message':it.getMessage(),
				  'senderName':it.getSenderName(),
				  'signOff':it.signoffWord,
				  'designation':it.getDesignation(),
				  'day':it.getDay(),
				  'date':it.getDate()? it.getDate().format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") :'date not' ]}
		render output as JSON
	
		
	}


}

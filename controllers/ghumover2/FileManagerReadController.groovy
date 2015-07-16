package ghumover2

import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile
import sun.misc.BASE64Decoder

class FileManagerReadController {

   
	
	def testSMS() {
		def rest = new RestBuilder()
		def resp = rest.post("http://api.mVaayoo.com/mvaayooapi/MessageCompose?user=vishal.sujanian@gmail.com:247619&senderID=TEST SMS&receipientno=9886326669&dcs=0&msgtxt=This is Test message for vishal&state=4")

			
		
		
		System.out.print("resp val : "+resp)
		render resp as JSON
	
	
		
		//render (view:'/')
	}
	
		def read(){
	
		println  "here"
		println params.data
		
		def params = JSON.parse(params.data)
		Long studentId = Long.parseLong(params.studentId)
		
		println studentId
		
	//Long studentId = Long.parseLong(params.data)
		FileInputStream fis = null;
		try {
			//store file the get data from that file
			def f = request.getFile('file')
			//validate file or do something crazy hehehe
			String albumTitle="test"
		//	String albumTitle = params.albumTitle
			//now transfer file
			def webrootDir = servletContext.getRealPath("/")+"images/profile" //app directory

			File file = new File(webrootDir)
			if(!file.exists()){
				file.mkdirs();
			}
			println servletContext.getContextPath()
			
			File fileDest = new File(webrootDir,f.getOriginalFilename())
			MyChildFile fil =new MyChildFile()
			//file.setFileId(file.name)
			String filename=f.getOriginalFilename()
			fil.setFileName(filename)
			fil.setFilePath(fileDest.path)
			fil.setDescription("profilepic")
			f.transferTo(fileDest)
			
			Student.executeUpdate("update Student set  studentPhoto = 'images/profile/"+filename +"' where studentId ="+studentId)
		
		
			fil.save()
			
		}
		catch(Exception e){
				println e.getMessage();
	
	
			}
	}
	
	
		
		
	def readImage(){
		
		File files=[]
		String albumTitle = params.school_id
		
		FileManager [] fileManagerList=new FileManager()
		FileManager fileManager=new FileManager()
		fileManager.setFileGroupType()
		fileManager.setFileGroupName(albumTitle)
		fileManager.setAlbumCoverUrl("http://saandeepani.in/images/xseed.png")
		fileManager.setPostedDate(Calendar.getInstance().getTime())
		fileManager.setFileCount("2")

		
		MyChildFile file2 =new MyChildFile()
		file2.setFileId("1243")
		file2.setFileName("activity")
		file2.setFilePath("http://saandeepani.in/images/inner-banner.jpg")
		file2.setDescription("schoolImage2")
		
		
		fileManager.addToFiles(file2)
		
		
		fileManagerList[0]=fileManager
		
		render  fileManagerList as JSON
	}

	
}
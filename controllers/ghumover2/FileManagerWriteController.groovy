package ghumover2

import grails.plugin.aws.s3.S3FileUpload;
import grails.converters.JSON
import grails.plugins.rest.client.RestBuilder
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile
import sun.misc.BASE64Decoder

class FileManagerWriteController {

	S3FileUpload st = new S3FileUpload()
	def fg = st.acl
	
	public def upload(){
	def s3file = new MyChildFile("testupload.txt").s3upload {
    path "Test1/"
}
	}
 
	def saveAlbumFiles(){
	
		def params = JSON.parse(params.data)
		String title = params.title
		
	
		FileInputStream fis = null;
		try {
			//store file the get data from that file
			def f = request.getFile('file')
			//validate file or do something crazy hehehe
			String albumTitle="test"
	
			//now transfer file
			String filepath="images/albums/"+title;
			def webrootDir = servletContext.getRealPath("/")+filepath //app directory
			
			FileManager fileManager=fileManager.findByFileGroupName(title)
			File file = new File(webrootDir)
			if(!file.exists()){
				file.mkdirs();
			
				if(fileManager==null){
					
					fileManager.setFileGroupType()
					fileManager.setFileGroupName(title)
					fileManager.setAlbumCoverUrl(filepath+'/'+filename)
					fileManager.setPostedDate(Calendar.getInstance().getTime())
					fileManager.setFileCount(0)
			
				
				}
			}
		
			
			
			File fileDest = new File(webrootDir,f.getOriginalFilename())
			MyChildFile fil =new MyChildFile()
			String filename=f.getOriginalFilename()
			fil.setFileName(filename)
			fil.setFilePath(fileDest.path)
			fil.setDescription("profilepic")
			f.transferTo(fileDest)
			fil.save()
			fileManager.addTo(fil)
			
		
			
		}
		catch(Exception e){
				println e.getMessage();
	
	
			}
		
	}
}

package ghumover2

import grails.converters.JSON
import com.amazonaws.util.json.JSONObject

import grails.plugins.rest.client.RestBuilder
import grails.converters.JSON
import groovy.json.JsonSlurper

import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import javassist.bytecode.stackmap.BasicBlock.Catch;
import java.awt.Color;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;





class MydataController {
	static	ExecutorService executer= Executors.newFixedThreadPool(10);
	def springSecurityService
	def index() {
		def rest = new RestBuilder()
		def resp = rest.post("https://api.pushbots.com/push/one"){
			header 'x-pushbots-appid', '550e9e371d0ab1de488b4569'
			header 'x-pushbots-secret', 'e68461d7755b0d3733b4b36717aea77d'
			json {
				token ="APA91bFS1Qg5lkFPOn6D6Liqc4pnUWcvTiyyn6XIW8RL5y-10rBt5PVg2XaVY5z7EaCGxAZN959OGpl3CYhK1pQVEm4AY7RD5DHSEz691VwOI8CElsgjnClHB7zfk5ioZ7SWAOlIVRA3"
				platform ="1"
				msg="Push one Notification from API  SArath 3/30call"
				sound="ding"
				badge="badge"
				payload="JSON"
			}
		}

		System.out.print("resp val : "+resp.json)
		render resp.json as JSON



		//render (view:'/')
	}

	
	


	def test() {

		//Long school_id = Long.parseLong(params.school_id)

	Long	school_id=1
		
		if(executer.isShutdown()){
			executer= Executors.newFixedThreadPool(10);
		}

		Role roleTeacher;
		Role roleParent;
		Role roleAdmin;

		roleTeacher = Role.find { authority=="ROLE_TEACHER" }
		if(null ==roleTeacher){
			roleTeacher = new Role(authority: ROLE_TEACHER)
			roleTeacher.save()
		}
		roleParent =Role.find { authority=="ROLE_PARENT" }
		if(null ==roleParent){
			roleParent = new Role(authority: ROLE_PARENT)
			roleParent.save()
		}
		roleAdmin =Role.find { authority=="ROLE_ADMIN" }
		if(null ==roleAdmin){
			roleAdmin = new Role(authority: ROLE_ADMIN)
			roleAdmin.save()
		}
		FileInputStream fis = null;
		try {

			def f = request.getFile('file')
			def webrootDir = servletContext.getRealPath("/")+"schoolData-"+school_id //app directory
			File file = new File(webrootDir)
			if(!file.exists()){
				file.mkdirs();
			}
			def errorFile = servletContext.getRealPath("/")+"schoolData-"+school_id+"-error" //app directory
			File file1 = new File(errorFile)
			if(!file1.exists()){
				file1.mkdirs();
			}
			File fileDest = new File(webrootDir,f.getOriginalFilename())
			File errfileDest = new File(errorFile,f.getOriginalFilename())
			f.transferTo(fileDest)
			/*
			UploadedDataErrorFileLocation uploadErrorlocation = new UploadedDataErrorFileLocation();
			uploadErrorlocation.schoolId=school_id;
			uploadErrorlocation.errorFileLocation= errorFile+"/"+f.getOriginalFilename();
			uploadErrorlocation.save(flush:true);*/
			School school =  School.get(school_id);
			fis = new FileInputStream(fileDest);
			FileOutputStream fileOut = new FileOutputStream(errfileDest);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet1 = workbook.getSheetAt(0);//
			Map<String ,Grade> classMap = new HashMap<String ,Grade>();
			Map gradeMap = new HashMap();
			Map teacherMap = new HashMap();
			Map<String,Department> demptIDMap = new HashMap<String,Department>();
			Map<String,ArrayList<Teacher>> deptMap = new HashMap<String,ArrayList<Teacher>>();
			def classobject= null;
			Grade grade = null;
			User user =null;
			def father = null;
			int errorCount =1;
			XSSFWorkbook errworkbook = new XSSFWorkbook();
			XSSFCellStyle style = errworkbook.createCellStyle();
			XSSFFont font = errworkbook.createFont();
			font.setColor(new XSSFColor(Color.red));
			style.setFont(font);
			XSSFSheet  errSheet1 = errworkbook.createSheet("sheet1");
			XSSFSheet  errSheet2 = errworkbook.createSheet("sheet2");
			XSSFRow row =errSheet1.createRow(0);
			XSSFCell classcell = row.createCell(0);
			classcell.setCellValue("Line No IN Origina Sheet");
			XSSFCell studentName = row.createCell(1);
			studentName.setCellValue("Sheet Name");
			XSSFCell parentName = row.createCell(2);
			parentName.setCellValue("Error Cause");

///abhinay
			//sheet1.getLastRowNum()
			for(int i=1;i<sheet1.getLastRowNum();i++){
				Guardian guardain =null;
				Guardian mother =null;
				XSSFRow hssfRow = sheet1.getRow(i);
				String errorCuse="";
				boolean isError = false;
				if(hssfRow != null){
					int s_regNo=0
					int s_grade= 2
					int s_nam= 3
					int s_dob= 4
					int f_nam= 5
					int f_mob= 6
					int f_email= 7
					int m_nam= 8
					int m_mob= 9
					int m_email= 10
					int s_addr= 11
					int place= 13
					int s_gen= 12
					int adm_date=1
					int mod_trans=8
					int med_cond=11
					int fee_type=0
					int bld_group=0
					int s_cat=13
					int s_rel=14
					if(hssfRow.getCell((short) s_grade) != null){
						if( (hssfRow.getCell((short) s_grade)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="class name is empty"
						}
						if( (hssfRow.getCell((short) s_grade)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="class name should be String"
						}

					}

					if(hssfRow.getCell((short) s_nam) != null){
						if( (hssfRow.getCell((short) s_nam)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Student name is empty"
						}
						if( (hssfRow.getCell((short) s_nam)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="Student name should be String"
						}

					 }
					println "------"+(hssfRow.getCell((short) f_nam)
						.getCellType())
					if(hssfRow.getCell((short) f_nam) != null){
						if( (hssfRow.getCell((short) f_nam)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Parent name is empty"
						}
						if( (hssfRow.getCell((short) f_nam)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="Parent name should be String"
						}

					}
					if(hssfRow.getCell((short) f_email) != null){
						if( (hssfRow.getCell((short) f_email)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Parent email is empty"
						}
						if( (hssfRow.getCell((short) f_email)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="Parent email should be String"
						}

					}
					if(hssfRow.getCell((short) f_mob) != null){
						if( (hssfRow.getCell((short) f_mob)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Mobile no is empty"
						}
					
						if( (hssfRow.getCell((short) f_mob)
						.getCellType() != 0)){

							isError =true;
							errorCuse ="Mobile no should be number"
						}
					}
						if(hssfRow.getCell((short) m_email) != null){
							if( (hssfRow.getCell((short)m_email)
							.getCellType() == 3)){
	
								isError =true;
								errorCuse ="Mother email is empty"
							}
						
							if( (hssfRow.getCell((short) m_email)
							.getCellType() != 1)){
	
								isError =true;
								errorCuse ="Mother email should be String"
							}
						}
						if(hssfRow.getCell((short) s_addr) != null){
							if( (hssfRow.getCell((short) s_addr)
							.getCellType() == 3)){
	
								isError =true;
								errorCuse ="Address is empty"
							}
						
							if( (hssfRow.getCell((short) s_addr)
							.getCellType() != 1)){
	
								isError =true;
								errorCuse ="Address should be String"
							}
						}

					if(isError){
						println i;

						ErrorLogInExcellUpload ob = new ErrorLogInExcellUpload();
						ob.lineNo = i+1
						ob.sheetName ="sheet1"
						ob.errorcause = errorCuse
						ob.userEmail = ""
						ob.schoolId=school_id
						ob.save(flush:true)

						XSSFRow row1 = errSheet1.createRow(errorCount);

						XSSFCell errno = row1.createCell(0);
						errno.setCellValue(i+1);

						XSSFCell SheetName = row1.createCell(1);
						SheetName.setCellValue("sheet1");
						XSSFCell errorName = row1.createCell(2);
						errorName.setCellStyle(style);

						errorName.setCellValue(errorCuse);



						errorCount++;
						continue ;
					}

					// do validation here


					String gaurdainEmail = hssfRow.getCell(f_email).getStringCellValue();
					if(gaurdainEmail != null){
						user = User.createCriteria().get  {
							or{
								eq("teacherEmailId",gaurdainEmail)
								eq("emailId",gaurdainEmail)
							}
						}
						if(null != user){
							if(user  instanceof  Guardian){
								guardain = user;
							}else{
								ErrorLogInExcellUpload ob = new ErrorLogInExcellUpload();
								ob.lineNo = i
								ob.sheetName ="sheet1"
								ob.errorcause = "Parent email id already used as a teacher "
								ob.userEmail = gaurdainEmail
								ob.schoolId=school_id
								ob.save(flush:true)
								continue ;
							}
						}
						String motherEmail = hssfRow.getCell(m_email).getStringCellValue();
						user = User.createCriteria().get  {
							or{
								eq("teacherEmailId",motherEmail)
								eq("emailId",motherEmail)
							}
						}
						if(null != user){
							if(user  instanceof  Guardian){
								mother = user;
							}else{
								ErrorLogInExcellUpload ob = new ErrorLogInExcellUpload();
								ob.lineNo = i
								ob.sheetName ="sheet1"
								ob.errorcause = "mother email id already used as a teacher "
								ob.userEmail = motherEmail
								ob.schoolId=school_id
								ob.save(flush:true)
								continue ;
							}
						}
						
						 
						//abhinay
						String class_grade = hssfRow.getCell(s_grade).getStringCellValue();
						int lenght = class_grade.length();
						Integer class_name = Integer.parseInt(class_grade.substring(0,lenght-1 ))
						String grade_name = class_grade.substring(lenght-1 )
						if(classMap.get(class_grade) == null){
							def sc = SchoolClass.createCriteria()
							classobject =	sc.get {
								and {
									eq("school.school_id",school_id)
									eq("className",class_name)
								}
							}
							if(classobject == null){
								SchoolClass schoolClass = new SchoolClass()
								schoolClass.className = class_name
								schoolClass.school = school
								schoolClass.classTags = school.tags +",\""+schoolClass.className+"\""
								schoolClass.save(flush:true);
								classobject = schoolClass
								grade = new Grade();
								grade.section = grade_name
								grade.name = (int)classobject.className
								grade.gradetags = schoolClass.classTags +",\""+schoolClass.className+"-"+grade.section+"\""
								grade.schoolClass =schoolClass
								grade.save(flush:true);
							}
							else{
								def gradeCriteria = Grade.createCriteria()

								grade =	gradeCriteria.get {
									and {
										eq("name",(int)classobject.className)
										eq("section",grade_name)
									}
								}
								//SchoolClass schoolClass =  SchoolClass.get(class_id)
								if(null == grade ){
									println 16;
									grade = new Grade();
									grade.section = grade_name
									grade.gradetags = classobject.classTags +",\""+classobject.className+"-"+grade.section+"\""
									grade.schoolClass =classobject
									grade.name = (int)classobject.className
									grade.save(flush:true);
								}
							}
							classMap.put(class_grade, grade)
							// for guardian
							if(null == guardain) {
								guardain =	new Guardian()
								guardain.name = hssfRow.getCell(f_nam).getStringCellValue();
								guardain.username=hssfRow.getCell(f_email).getStringCellValue();
								guardain.password= "123"
								guardain.educational_qualification= ""
								guardain.designation= ""
								guardain.profession= ""
								guardain.emailId= hssfRow.getCell(f_email).getStringCellValue();
								guardain.officeNumber= ""
								long fpn= hssfRow.getCell(f_mob).getNumericCellValue();
								guardain.mobileNumber= String.valueOf(fpn);
								guardain.save(flush:true);
								//asign role to parent
								new UserRole(user: guardain, role: roleParent).save();
							}
							if(null == mother){
							mother =	new Guardian()
							mother.name = hssfRow.getCell(m_nam).getStringCellValue();
							mother.username=hssfRow.getCell(m_email).getStringCellValue();
							mother.school_id = school_id
							mother.password= "123"
							mother.educational_qualification= ""
							mother.designation= ""
							mother.profession= ""
							mother.emailId= hssfRow.getCell(m_email).getStringCellValue();
							mother.officeNumber= ""
							long mpn= hssfRow.getCell(m_mob).getNumericCellValue();
								mother.mobileNumber= String.valueOf(mpn)

							mother.save(flush:true);
							new UserRole(user: mother, role: roleParent).save();
							}
							
							
							
							
							
							// add student
							Student student =  new Student();
							student.grade = grade
							student.registerNumber =  hssfRow.getCell(s_regNo).getStringCellValue();
							student.studentName  =hssfRow.getCell(s_nam).getStringCellValue();
							student.gender= hssfRow.getCell(s_gen).getStringCellValue();
							String date_s =(hssfRow.getCell(s_dob).getStringCellValue());
							SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
							Date date = dt.parse(date_s);
							student.dob = date;
							student.studentPhoto="photo.jpg"
							student.no_of_siblings=2
							student.present_guardian="Father"
							student.present_address =new Address(address: hssfRow.getCell(s_addr).getStringCellValue() , landmark: "" , place: "").save()
							student.modeOfTransport=""//hssfRow.getCell(mod_trans).getStringCellValue();
							student.bloodGroup=""//hssfRow.getCell(bld_group).getStringCellValue();
							student.feeType=""//hssfRow.getCell(fee_type).getStringCellValue();
							student.medicalCondition=""//hssfRow.getCell(med_cond).getStringCellValue();
							student.category=hssfRow.getCell(s_cat).getStringCellValue();
							student.religion=hssfRow.getCell(s_rel).getStringCellValue();
							def dat=hssfRow.getCell(adm_date).getStringCellValue();
							Date date_e = dt.parse(dat);
							student.admDate = date_e;
							student.save(flush:true);

							father = Guardian.findByUsername(hssfRow.getCell(f_email).getStringCellValue())
							 mother = Guardian.findByUsername(motherEmail)
							String father_tags  = father.tags;
							String mother_tags  = mother.tags;
							student.setAsFather( father )
							student.setAsMother( mother )
							if(father_tags == null){
								father_tags = grade.gradetags +",\"G-"+guardain.username+"\",\"S-"+student.studentId+"\""
							}else{
								father_tags = father.tags+",\"s-"+student.studentId+"\""
							}
							Guardian.executeUpdate("Update Guardian set tags = '"+father_tags+"' where username ='"+father.username+"'")
						
							if(mother_tags == null){
								mother_tags = grade.gradetags +",\"G-"+mother.username+"\",\"S-"+student.studentId+"\""
							}else{
								mother_tags = mother.tags+",\"s-"+student.studentId+"\""
							}
							Guardian.executeUpdate("Update Guardian set tags = '"+mother_tags+"' where username ='"+mother.username+"'")
						
							
							
							
							
							}else{
							println 18;
							if(null == guardain) {
								guardain =	new Guardian()
								guardain.name = hssfRow.getCell(f_nam).getStringCellValue();
								guardain.username=hssfRow.getCell(f_email).getStringCellValue();
								guardain.school_id = school_id
								guardain.password= "123"
								guardain.educational_qualification= ""
								guardain.designation= ""
								guardain.profession= ""
								guardain.emailId= hssfRow.getCell(f_email).getStringCellValue();
								guardain.officeNumber= ""
								long fpn= hssfRow.getCell(f_mob).getNumericCellValue();
								guardain.mobileNumber= String.valueOf(fpn)
								guardain.mobileNumber= hssfRow.getCell(f_mob).getNumericCellValue();

								guardain.save(flush:true);

								new UserRole(user: guardain, role: roleParent).save();

							}
							if(null == mother){
								mother =	new Guardian()
								mother.name = hssfRow.getCell(m_nam).getStringCellValue();
								mother.username=hssfRow.getCell(m_email).getStringCellValue();
								mother.school_id = school_id
								mother.password= "123"
								mother.educational_qualification= ""
								mother.designation= ""
								mother.profession= ""
								mother.emailId= hssfRow.getCell(m_email).getStringCellValue();
								mother.officeNumber= ""
								long mpn= hssfRow.getCell(m_mob).getNumericCellValue();
								mother.mobileNumber= String.valueOf(mpn)
	
								mother.save(flush:true);
								new UserRole(user: mother, role: roleParent).save();
								}

							Student student =  new Student();
							student.grade =classMap.get(class_grade)
							student.registerNumber =  hssfRow.getCell(s_regNo).getStringCellValue();
							student.studentName  = hssfRow.getCell(s_nam).getStringCellValue();
							student.gender= hssfRow.getCell(s_gen).getStringCellValue();
							String date_s =(hssfRow.getCell(s_dob).getStringCellValue());
							SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
							Date date = dt.parse(date_s);
							student.dob = date;
							student.studentPhoto="photo.jpg"
							student.no_of_siblings=2
							student.present_guardian="Father"
							student.present_address =new Address(address: hssfRow.getCell(s_addr).getStringCellValue() , landmark: "" , place:"" ).save()
							student.modeOfTransport=""//hssfRow.getCell(mod_trans).getStringCellValue();
							student.bloodGroup=""//hssfRow.getCell(bld_group).getStringCellValue();
							student.feeType=""//hssfRow.getCell(fee_type).getStringCellValue();
							student.medicalCondition=""//hssfRow.getCell(med_cond).getStringCellValue();
							student.category=hssfRow.getCell(s_cat).getStringCellValue();
							student.religion=hssfRow.getCell(s_rel).getStringCellValue();
							def dat=hssfRow.getCell(adm_date).getStringCellValue();
							Date date_e = dt.parse(dat);
							student.admDate = date_e;

							student.save(flush:true);

							father = Guardian.findByUsername(hssfRow.getCell(f_email).getStringCellValue())
							mother = Guardian.findByUsername(hssfRow.getCell(m_email).getStringCellValue())
							
							String father_tags  = father.tags;
							String mother_tags  = mother.tags;
						
							student.setAsFather( father )
						
							if(father_tags == null){
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								father_tags = grade.gradetags +",\"G-"+guardain.username+"\",\"S-"+student.studentId+"\""
							}else{
								father_tags = father.tags+",\"s-"+student.studentId+"\""
							}
							Guardian.executeUpdate("Update Guardian set tags = '"+father_tags+"' where username ='"+father.username+"'")
							
							if(mother_tags == null){
								mother_tags = grade.gradetags +",\"G-"+mother.username+"\",\"S-"+student.studentId+"\""
							}else{
								mother_tags = mother.tags+",\"s-"+student.studentId+"\""
							}
							Guardian.executeUpdate("Update Guardian set tags = '"+mother_tags+"' where username ='"+mother.username+"'")

						}
					/*	if(null == father){

							father = Guardian.findByUsername(hssfRow.getCell(3).getStringCellValue())

						}
						String emailmessage = "Hi ,your user name is "+father.username+"and password is pass@123 ,after login please change your password (${new Date().format('dd/MM/yyyy HH:mm')})"
						//executer.execute(new EmailSendThread("jayantamca10@gmail.com",father.username,emailmessage));
						if(null == mother){

							mother = Guardian.findByUsername(hssfRow.getCell(6).getStringCellValue())

						}*/
						//String emailmessage = "Hi ,your user name is "+mother.username+"and password is pass@123 ,after login please change your password (${new Date().format('dd/MM/yyyy HH:mm')})"
						//executer.execute(new EmailSendThread("jayantamca10@gmail.com",mother.username,emailmessage));

					}

				}

			}
			XSSFSheet sheet2 = workbook.getSheetAt(1);
			Long userId= User.createCriteria().get { projections { max "id" } } as Long
			//sheet2.getLastRowNum() sheet2.getLastRowNum()
			Department dept =null; int z=1;
			for(int i=1;i<=sheet2.getLastRowNum();i++){
				String errorCuse="";
				boolean isError = false;
				XSSFRow hssfRow2 = sheet2.getRow(i);
				if(hssfRow2 != null){
					int e_nam=1
					int e_code=3
					int e_doj=4
					int e_mob=5
					int e_quali=7
					int e_email=8
					int e_post=6
					int e_addr=9
					int e_dpt=6
					int cl_teach=10
					int dob=2
					

					// do error handling here
					if(hssfRow2.getCell((short) e_dpt) != null){
						if( (hssfRow2.getCell((short) e_dpt)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="department name is empty"
						}
						if( (hssfRow2.getCell((short) e_dpt)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="department name should be String"
						}

					}

					if(hssfRow2.getCell((short) e_nam) != null){
						if( (hssfRow2.getCell((short) e_nam)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Teacher name is empty"
						}
						if( (hssfRow2.getCell((short) e_nam)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="Teacher name should be String"
						}

					}

					if(hssfRow2.getCell((short) e_email) != null){
						if( (hssfRow2.getCell((short) e_email)
						.getCellType() == 3)){

							isError =true;
							errorCuse ="Teacher email is empty"
						}
						if( (hssfRow2.getCell((short) e_email)
						.getCellType() != 1)){

							isError =true;
							errorCuse ="Teacher email should be String"
						}

					}
				/*	if(hssfRow2.getCell((short) cl_teach) != null){
						if( (hssfRow2.getCell((short) cl_teach)
						.getCellType() != 3)){

							if( (hssfRow2.getCell((short) cl_teach)
							.getCellType() != 1)){

								isError =true;
								errorCuse ="class name should be String"
							}
						}

					}  */

					if(isError){

						ErrorLogInExcellUpload ob = new ErrorLogInExcellUpload();
						ob.lineNo = i+1
						ob.sheetName ="sheet2"
						ob.errorcause = errorCuse
						ob.userEmail = ""
						ob.schoolId=school_id
						ob.save(flush:true)

						XSSFRow row1 = errSheet1.createRow(errorCount);

						XSSFCell errno = row1.createCell(0);
						errno.setCellValue(i+1);

						XSSFCell SheetName = row1.createCell(1);
						SheetName.setCellValue("sheet2");
						XSSFCell errorName = row1.createCell(2);
						errorName.setCellStyle(style);

						errorName.setCellValue(errorCuse);
						errorCount++;
						continue ;
					}



					if(null != hssfRow2.getCell(e_dpt)){
						String dept_name = hssfRow2.getCell(e_dpt).getStringCellValue()
						println dept_name
						if(demptIDMap.get(dept_name) == null) {
							def deptCriteria = Department.createCriteria()
							dept  =	deptCriteria.get {
								and {
									eq("dept_name",dept_name)
									eq("school_id",school_id)
								}
							}
							try{
								if(null == dept){
									dept = new Department()
									dept.dept_name = hssfRow2.getCell(e_dpt).getStringCellValue()

									dept.school_id = school_id
									dept.save(flush:true);

								}}
							catch(Exception e){
								println e.getMessage() +"-------------- error arise";
								e.printStackTrace()
							}

							demptIDMap.put(dept_name, dept)
						}else{
							dept = demptIDMap.get(dept_name)
						}

						//addd teacher

						String teachermail = hssfRow2.getCell(e_email).getStringCellValue()
						user = User.createCriteria().get  {
							or{
								eq("teacherEmailId",teachermail)
								eq("emailId",teachermail)
							}

						}
						if(null == user){
							Teacher t = new Teacher()
							if(null == userId){
								userId =1
							}else{
								userId = userId+1}

							t.school_id = school_id
							t.teacherName = hssfRow2.getCell(e_nam).getStringCellValue()
							t.teacherPhoto =""
							t.teacherEmailId =teachermail
							  
							//t.phoneNo = new BigDecimal(hssfRow2.getCell(e_mob).getNumericCellValue()).toPlainString();
							long ph= hssfRow2.getCell(e_mob).getNumericCellValue()
							t.phoneNo = String.valueOf(ph)
							t.username= teachermail
							t.password= "123"
							t.empCode= hssfRow2.getCell(e_code).getStringCellValue()
							t.qualification= hssfRow2.getCell(e_quali).getStringCellValue()
							SimpleDateFormat dt = new SimpleDateFormat("dd-mm-yyyy");
							def date_t=hssfRow2.getCell(dob).getStringCellValue()
							Date datee=dt.parse(date_t)
							t.DOB=datee
							def date_s =(hssfRow2.getCell(e_doj).getStringCellValue());
							
							Date date = dt.parse(date_s);
							t.doj= date
							t.tAddr= hssfRow2.getCell(e_addr).getStringCellValue()
							t.post= hssfRow2.getCell(e_post).getStringCellValue()
							t.deviceToken = null;
							t.save(flush:true);
							// add teacher to section
							//asign role to teacher
							new UserRole(user: t, role: roleTeacher).save();

						XSSFCell cell3value = hssfRow2.getCell(cl_teach)
							if(null != cell3value ){
								String class_grade = cell3value.getStringCellValue();
								int lenght = class_grade.length();
								Integer class_name = Integer.parseInt(class_grade.substring(0,lenght-1 ))
								String grade_name = class_grade.substring(lenght-1 )
								Grade grade1 = classMap.get(cell3value.getStringCellValue())
								if(null != grade1){
									grade1.addToTeachers(t)
									grade1.save(flush:true)

									String mathew_tags  = t.tags;
									Grade.executeUpdate("Update Grade set classTeacherId = "+t.id+" where gradeId ="+grade1.gradeId)
									if(mathew_tags == null ){
										mathew_tags = grade1.gradetags +",\"T-T\",\"T-"+t.id+","+t.username+"\"," +dept.dept_tags
									}else{
										if(-1< mathew_tags.indexOf("T_T")){
											mathew_tags = t.tags+",\"T-"+t.id+","+t.username+"\"," +dept.dept_tags
										}else{
											mathew_tags = grade1.gradetags +",\"T-T\",\"T-"+t.id+","+t.username+"\"," +t.tags+","+dept.dept_tags
										}
									}
									t.tags  = mathew_tags;
									t.save(flush:true);
									dept.addToTeachers(t)
									dept.save(flush:true);

								}else{
									// if already calls is created that time checking
									def sc = SchoolClass.createCriteria()
									classobject =	sc.get {
										and {
											eq("school.school_id",school_id)
											eq("className",class_name)
										}
									}
									if(classobject == null){
										SchoolClass schoolClass = new SchoolClass()
										schoolClass.className = class_name
										schoolClass.school = school
										schoolClass.classTags = school.tags +",\""+schoolClass.className+"\""
										schoolClass.save(flush:true);
										classobject = schoolClass
										grade = new Grade();
										grade.section = grade_name
										grade.name = (int)classobject.className
										grade.gradetags = schoolClass.classTags +",\""+schoolClass.className+"-"+grade.section+"\""
										grade.schoolClass =schoolClass
										grade.save(flush:true);
									}
									else{
										def gradeCriteria = Grade.createCriteria()

										grade =	gradeCriteria.get {
											and {
												eq("name",(int)classobject.className)
												eq("section",grade_name)
											}
										}
										//SchoolClass schoolClass =  SchoolClass.get(class_id)
										if(null == grade ){
											grade = new Grade();
											grade.section = grade_name

											grade.gradetags = classobject.classTags +",\""+classobject.className+"-"+grade.section+"\""
											grade.schoolClass =classobject
											grade.name = (int)classobject.className
											grade.save(flush:true);
										}
									}
									grade.addToTeachers(t)
									grade.save(flush:true)
									Grade.executeUpdate("Update Grade set classTeacherId = "+t.id+" where gradeId ="+grade.gradeId)
									String mathew_tags  = t.tags;
									println " ---- "+dept.dept_tags
									println "++++++"+grade.gradetags
									if(mathew_tags == null ){
										mathew_tags = grade.gradetags +",\"T-T\",\"T-"+t.id+","+t.username+"\"," +dept.dept_tags
									}else{
										if(-1< mathew_tags.indexOf("T_T")){
											mathew_tags = t.tags+",\"T-"+t.id+","+t.username+"\"," +dept.dept_tags
										}else{
											mathew_tags = grade.gradetags +",\"T-T\",\"T-"+t.id+","+t.username+"\"," +t.tags+","+dept.dept_tags
										}

									}
									t.tags  = mathew_tags;
									t.save(flush:true);
									dept.addToTeachers(t)
									dept.save(flush:true);
									classMap.put(class_grade, grade)
								}
							}else{
							t.tags  = t.id+","+t.username+"\","+dept.dept_tags
							t.save(flush:true);
							dept.addToTeachers(t)
							dept.save(flush:true);
							}

							//String emailmessage = "Hi ,your user name is "+t.username+"and password is pass@123 ,after login please change your password (${new Date().format('dd/MM/yyyy HH:mm')})"
							//executer.execute(new EmailSendThread("jayantamca10@gmail.com",t.username,emailmessage));


						}else{
							ErrorLogInExcellUpload ob = new ErrorLogInExcellUpload();

							ob.lineNo = i+1
							ob.sheetName ="sheet2"
							ob.errorcause = "email id already used "
							ob.userEmail = teachermail
							ob.schoolId=school_id
							ob.save(flush:true)

							// email id already used by other
						}
					}
				}else{

				}

println z++
			}

			errworkbook.write(fileOut);
			fileOut.flush();
			fileOut.close();


		}catch(Exception e){
		e.printStackTrace()
			println e.getCause();
			println e.getMessage();
			
			
			


		}
		Map ob = new HashMap();
		ob.putAt("datauploaded", "success");

		render ob as JSON

	}


	public void saveMailStatus(String sesEmailResponse ,String username){
		User.withTransaction{ status ->
			User.executeUpdate("Update Guardian set sesEmailResponse = '"+sesEmailResponse+"' where username ='"+username+"'")
		}
	}


	def upload2 () {/*

		Long school_id = Long.parseLong(params.school_id)


		FileInputStream fis = null;
		try {

			def f = request.getFile('excelFile')
			def webrootDir = servletContext.getRealPath("/")+"schoolData-"+school_id //app directory
			File file = new File(webrootDir)
			if(!file.exists()){
				file.mkdirs();
			}
			def errorFile = servletContext.getRealPath("/")+"schoolData-"+school_id+"-error" //app directory
			File file1 = new File(errorFile)
			if(!file1.exists()){
				file1.mkdirs();
			}
			File fileDest = new File(webrootDir,f.getOriginalFilename())
			File errfileDest = new File(errorFile,"aa"+f.getOriginalFilename())
			f.transferTo(fileDest)
			School school =  School.get(school_id);
			fis = new FileInputStream(fileDest);
			println errorFile;
			FileOutputStream fileOut = new FileOutputStream(errfileDest);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet1 = workbook.getSheetAt(0);//

			XSSFWorkbook errworkbook = new XSSFWorkbook();
			XSSFCellStyle style = errworkbook.createCellStyle();
			XSSFFont font = errworkbook.createFont();
			font.setColor(new XSSFColor(Color.red));
			style.setFont(font);
			XSSFSheet  errSheet1 = errworkbook.createSheet("sheet1");
			//XSSFSheet  errSheet2 = workbook.createSheet("sheet2");
			XSSFRow row =errSheet1.createRow(0);
			XSSFCell classcell = row.createCell(0);
			classcell.setCellValue("Class");
			XSSFCell studentName = row.createCell(1);
			studentName.setCellValue("Student Name");
			XSSFCell parentName = row.createCell(2);
			parentName.setCellValue("Parent Name");

			XSSFCell parentEmail = row.createCell(3);
			parentEmail.setCellValue("Email");
			XSSFCell mobileNo = row.createCell(4);
			mobileNo.setCellValue("Mobile No");

			for(int i=1;i<sheet1.getLastRowNum();i++){


				XSSFRow row1 = errSheet1.createRow(i);
				if(row1 != null){
					XSSFCell classcell1 = row1.createCell(0);
					classcell1.setCellStyle(style);
					classcell1.setCellValue("Class");

					XSSFCell studentName1 = row1.createCell(1);
					studentName1.setCellValue("Student Name");
					XSSFCell parentName1 = row1.createCell(2);
					parentName1.setCellValue("Parent Name");

					XSSFCell parentEmail1 = row1.createCell(3);
					parentEmail1.setCellValue("Email");
					XSSFCell mobileNo1 = row1.createCell(4);
					mobileNo1.setCellValue("Mobile No");
				}
			}
			errworkbook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		}catch(Exception e){

		}


			println "inside sentmail"
		 if(executer.isShutdown()){
		 println "pool terminated"
		 executer= Executors.newFixedThreadPool(10);
		 }
		 for(int i=0;i<1;i++){
		 //	savenailStatus("dsagdjg--------------")
		 executer.execute(new EmailSendThread("jayantamca10@gmail.com","Babyrani.Devi@test.com","sesid"));
		 }

		render new HashMap() as JSON
	*/}
	
	def registerForpushApp() {
		
		def device_platform = params.platform
		def device_token = params.token;
		User user = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
		//	Teacher teacher = Teacher.findByUsername(user.username)
	   
			//will get the username from spring security but for now i am not using
			String username = user.username
		 user = User.findByUsername(username)
		String userTags = user.tags
		User.executeUpdate("Update User set deviceToken = '"+device_token+"',tagRegister=true,platform='"+device_platform+"' where username ='"+username+"'")

		def rest = new RestBuilder()
		def resp = rest.put("https://api.pushbots.com/deviceToken"){
			header 'x-pushbots-appid', '550e9e371d0ab1de488b4569'
			header 'x-pushbots-secret', 'e68461d7755b0d3733b4b36717aea77d'
			json {
				token =device_token
				platform =device_platform
				alias =username
				tag = [userTags]
				payload="JSON"

			}


		}
		println "------------------------------------ [${device_token}]"
		println "------------------------------------ ${device_platform}"
		println resp.json as JSON

		Map ob = new HashMap();
		ob.put("status", true)
		ob.put("message", "success")

		render ob as JSON
	}
	def mailService;

	class EmailSendThread implements Runnable{
		String toEmail;
		String username;
		String emailMessage;
		EmailSendThread( String toEmail,String username,String emailMessage){
			this.username = username
			this.toEmail=toEmail
			this.emailMessage = emailMessage
		}
		public void run(){
			try{
				println "inside try"
				def sesEmailResponse = sesMail {
					from "jayantamca10@gmail.com"

					to toEmail
					subject "MyChild Account Details"
					body  emailMessage
				}
				saveMailStatus(sesEmailResponse,username)

			}catch(Exception e){
				e.printStackTrace();
			}

		}
	}

	def addStudent(){
		def jsonObject = request.JSON
		
		def jsonSlurper = new JsonSlurper()
def object = jsonSlurper.parseText(jsonObject.toString())

assert object instanceof Map
assert object.students.class == Student
assert object.address.class == Address
assert object.father.class == Guardian
		
		println "------------------------------------ ${jsonObject}"
		println "------------------------------------ ${jsonObject.toString()}"

		render jsonObject
		
		

	
	
	
			//Long school_id = Long.parseLong(params.school_id)
	
		Long	school_id=1
			
			if(executer.isShutdown()){
				executer= Executors.newFixedThreadPool(10);
			}
	
			Role roleTeacher;
			Role roleParent;
			Role roleAdmin;
	
			roleTeacher = Role.find { authority=="ROLE_TEACHER" }
			if(null ==roleTeacher){
				roleTeacher = new Role(authority: ROLE_TEACHER)
				roleTeacher.save()
			}
			roleParent =Role.find { authority=="ROLE_PARENT" }
			if(null ==roleParent){
				roleParent = new Role(authority: ROLE_PARENT)
				roleParent.save()
			}
			roleAdmin =Role.find { authority=="ROLE_ADMIN" }
			if(null ==roleAdmin){
				roleAdmin = new Role(authority: ROLE_ADMIN)
				roleAdmin.save()
			}
			FileInputStream fis = null;
			try {
	
			
				
				/*
				UploadedDataErrorFileLocation uploadErrorlocation = new UploadedDataErrorFileLocation();
				uploadErrorlocation.schoolId=school_id;
				uploadErrorlocation.errorFileLocation= errorFile+"/"+f.getOriginalFilename();
				uploadErrorlocation.save(flush:true);*/
				School school =  School.get(school_id);
				fis = new FileInputStream(fileDest);
				FileOutputStream fileOut = new FileOutputStream(errfileDest);
				XSSFWorkbook workbook = new XSSFWorkbook(fis);
				XSSFSheet sheet1 = workbook.getSheetAt(0);//
				Map<String ,Grade> classMap = new HashMap<String ,Grade>();
				Map gradeMap = new HashMap();
				Map teacherMap = new HashMap();
				Map<String,Department> demptIDMap = new HashMap<String,Department>();
				Map<String,ArrayList<Teacher>> deptMap = new HashMap<String,ArrayList<Teacher>>();
				def classobject= null;
				Grade grade = null;
				User user =null;
				def father = null;
				int errorCount =1;
			

	
	///abhinay
				//sheet1.getLastRowNum()
				//for(int i=1;i<sheet1.getLastRowNum();i++){
					Guardian guardain =null;
					Guardian mother =null;
				
					String errorCuse="";
					boolean isError = false;
					if(hssfRow != null){
					
	
					
						println "------"+(hssfRow.getCell((short) 2)
							.getCellType())
					
						
	
	
						// do validation here
	
	
						String gaurdainEmail = hssfRow.getCell(3).getStringCellValue();
						if(gaurdainEmail != null){
							user = User.createCriteria().get  {
								or{
									eq("teacherEmailId",gaurdainEmail)
									eq("emailId",gaurdainEmail)
								}
							}
							if(null != user){
								if(user  instanceof  Guardian){
									guardain = user;
								}
							}
							String motherEmail = hssfRow.getCell(6).getStringCellValue();
							user = User.createCriteria().get  {
								or{
									eq("teacherEmailId",motherEmail)
									eq("emailId",motherEmail)
								}
							}
							if(null != user){
								if(user  instanceof  Guardian){
									mother = user;
								}
							}
							
							 
							//abhinay
							String class_grade = hssfRow.getCell(0).getStringCellValue();
							int lenght = class_grade.length();
							Integer class_name = Integer.parseInt(class_grade.substring(0,lenght-1 ))
							String grade_name = class_grade.substring(lenght-1 )
							if(classMap.get(class_grade) == null){
								def sc = SchoolClass.createCriteria()
								classobject =	sc.get {
									and {
										eq("school.school_id",school_id)
										eq("className",class_name)
									}
								}
								
								
									def gradeCriteria = Grade.createCriteria()
	
									grade =	gradeCriteria.get {
										and {
											eq("name",(int)classobject.className)
											eq("section",grade_name)
										}
									}
									//SchoolClass schoolClass =  SchoolClass.get(class_id)
									
								
								classMap.put(class_grade, grade)
								// for guardian
								if(null == guardain) {
									guardain =	new Guardian()
									 guardian
									guardain.save(flush:true);
									//asign role to parent
									new UserRole(user: guardain, role: roleParent).save();
								}
								if(null == mother){
								mother =	new Guardian()
						
	
								mother.save(flush:true);
								new UserRole(user: mother, role: roleParent).save();
								}
								
								
								
								
								
								// add student
								Student student =  new Student();
								student.grade = grade
								student.registerNumber = ""
								student.studentName  = hssfRow.getCell(1).getStringCellValue();
								student.gender= ""
								student.dob = new Date();
								student.studentPhoto="photo.jpg"
								student.no_of_siblings=2
								student.present_guardian="Father"
								//student.present_address =new Address(address: "Sample Address" , landmark: "Cochin" , place: "Kerala").save()
								student.save(flush:true);
	
								father = Guardian.findByUsername(hssfRow.getCell(3).getStringCellValue())
								 mother = Guardian.findByUsername(motherEmail)
								String father_tags  = father.tags;
								String mother_tags  = mother.tags;
								student.setAsFather( father )
								student.setAsMother( mother )
								if(father_tags == null){
									father_tags = grade.gradetags +",\"G\",\"S-"+student.studentId+"\""
								}else{
									father_tags = father.tags+",\"s-"+student.studentId+"\""
								}
								Guardian.executeUpdate("Update Guardian set tags = '"+father_tags+"' where username ='"+father.username+"'")
							
								if(mother_tags == null){
									father_tags = grade.gradetags +",\"G\",\"S-"+student.studentId+"\""
								}else{
									mother_tags = mother.tags+",\"s-"+student.studentId+"\""
								}
								Guardian.executeUpdate("Update Guardian set tags = '"+mother_tags+"' where username ='"+mother.username+"'")
							
								
								
								
								
								}
						
						}
	
					}
	
			//	}
	
	
	
	
			}catch(Exception e){
				println e.getCause();
				println e.getMessage();
	
	
			}
			Map ob = new HashMap();
			ob.putAt("datauploaded", "success");
	
			render ob as JSON
	
		
	render true
		
	}
}



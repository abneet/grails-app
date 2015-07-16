package ghumover2
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.grails.plugins.excelimport.*
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

class StudentDataExcelController {
    def excelImportService
    def index() {

r

    }


    def saveExcelData()
    {


        Map CONFIG_STUDENT_COLUMN_MAP = [
                sheet:'Sheet1',
                startRow: 1,
                columnMap:  [
                        //Col, Map-Key
                        'A':'registerNumber',
                        'B':'studentName',
                        'C':'gender',
                        'D':'dob',
                        'E':'grade',
                        'F':'section',
                                'G':'address',
                                'H':'place',
                                'I':'landmark',
                                    'J':'no_of_siblings',
                                    'K':'father_name',
                                    'L':'father_education',
                                    'M':'father_profession',
                                    'N':'father_designation',
                                    'O':'father_mobile',
                                    'P':'father_email',
                                    'Q':'father_office_number',
                                        'R':'mother_name',
                                        'S':'mother_education',
                                        'T':'mother_profession',
                                        'U':'mother_designation',
                                        'V':'mother_mobile',
                                        'W':'mother_email',
                                        'X':'mother_office_number',
                                                'Y':'local_guardian_name',
                                                'Z':'local_guardian_education',
                                                'AA':'local_guardian_profession',
                                                'AB':'local_guardian_designation',
                                                'AC':'local_guardian_mobile',
                                                'AD':'local_guardian_email',
                                                'AE':'local_guardian_office_number',
                                                'AF':'present_guardian'

                ]
        ]


        MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
        CommonsMultipartFile file = (CommonsMultipartFile) mpr.getFile("file");
        Workbook workbook = WorkbookFactory.create(file.inputStream)
        //Iterate through bookList and create/persists your domain instances
        def studentList = excelImportService.columns(workbook, CONFIG_STUDENT_COLUMN_MAP)

        def student , parent , father , mother ,local_guardian , address
        Grade grade
        int gradeId


        def emptyList = []
        def List = studentList.toList()
        List.each() { 	item ->


                gradeId =  item.grade.intValue()


                address = new Address(address: item.address , place: item.place , landmark: item.landmark).save()
                grade = Grade.findByNameAndSection(gradeId , item.section)
                student = new Student(registerNumber: item.registerNumber.intValue() , studentName:item.studentName , gender:item.gender , present_address : address , no_of_siblings : item.no_of_siblings , dob:item.dob , grade: grade , present_guardian: item.present_guardian  )
                student.save()

                student.setAsFather(new Guardian(username: item.father_email , password: "123" , name: item.father_name , educational_qualification :item.father_education , profession: item.father_profession , designation:item.father_designation , mobileNumber : item.father_mobile , emailId: item.father_email , officeNumber : item.father_office_number ).save() )
                 student.setAsMother(new Guardian(username: item.mother_email , password: "123" ,name: item.mother_name , educational_qualification :item.mother_education , profession: item.mother_profession , designation:item.mother_designation , mobileNumber : item.mother_mobile , emailId: item.mother_email , officeNumber : item.mother_office_number ).save() )
                if(item.present_guardian == 'Local guardian')
                 {
                     student.setAsLocalGuardian(new Guardian(username: item.local_guardian_email , password: "123" ,name: item.local_guardian_name , educational_qualification :item.local_guardian_education , profession: item.local_guardian_profession , designation:item.local_guardian_designation , mobileNumber : item.local_guardian_mobile , emailId: item.local_guardian_email , officeNumber : item.local_guardian_office_number ).save() )

                 }






        }


render "Data saved"


    }
}

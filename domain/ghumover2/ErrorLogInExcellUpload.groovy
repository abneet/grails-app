package ghumover2

class ErrorLogInExcellUpload {
	Long errorId;
	int lineNo;
	String sheetName;
	String errorcause;
	String userEmail;
	Long schoolId;

    static constraints = {
    }
	static mapping = {
		id generator: 'increment',name: 'errorId'


	}
}

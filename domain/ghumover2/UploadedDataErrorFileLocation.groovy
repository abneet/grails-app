package ghumover2

class UploadedDataErrorFileLocation {
	Long upDataId;
	Long schoolId
	String errorFileLocation

    static mapping = {
        id generator: 'increment',name: 'upDataId'


    }
}

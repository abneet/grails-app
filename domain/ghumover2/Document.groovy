package ghumover2

class Document {
    String filename
    byte[] filedata
    Date uploadDate = new Date()
    static constraints = {
        filename(blank:false,nullable:false)
        filedata(blank: true, nullable:true, maxSize:1073741824)
    }
}
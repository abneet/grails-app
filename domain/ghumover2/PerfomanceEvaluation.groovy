package ghumover2

class PerfomanceEvaluation {

    static belongsTo = [teacher:Teacher]
    String filename
    byte[] filedata
    Integer year
    Date uploadDate = new Date()
    static constraints = {
        filename(blank:false,nullable:false)
        filedata(blank: true, nullable:true, maxSize:1073741824)
    }


}

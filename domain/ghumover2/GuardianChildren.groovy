package ghumover2

class GuardianChildren {
    Guardian guardian
    Student student
    String guardianType

    static constraints = {
        student unique: ['guardian' , 'guardianType']
    }

}
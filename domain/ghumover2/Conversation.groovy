package ghumover2

import org.grails.databinding.BindingFormat

class Conversation {


    String fromId
    String toId
	String fromName
	String toName
    static belongsTo = [fromId:User , toId:User]
    static mappedBy = [fromId: 'username' , toId: 'username']

    static hasMany = [messages:Message]

    Long threadId
    String title
    Boolean inTrash
    Boolean isRead
    @BindingFormat("dd-MM-yyyy")
    Date toDate
    @BindingFormat("dd-MM-yyyy")




    static mapping = {
        id generator: 'increment',name: 'threadId'


    }



    static constraints = {
         toDate(nullable: true)
		

    }




}

package ghumover2

class Image {

	byte[] imageFile
	String  imageName
    static constraints = {
		imageFile nullable: true, blank: true, maxSize: 1024 * 1024 * 20; //20MB
		imageName nullable: true, blank: true;
		}
	
}

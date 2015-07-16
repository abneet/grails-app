package ghumover2

class UserRoleDetailsController {

    def index() {}

    def addRole()
    {
        try {

            User user = User.get(Integer.parseInt(params.id))
            Role role = Role.get(Integer.parseInt(params.role))
            def save = new UserRole(user: user,role: role).save()
            if(save)
            {
                render "OK"
            }

        }
        catch (Exception e)
        {
                render e
        }

    }
}

import hudson.model.User
current_user = User.current()
println current_user.getId()

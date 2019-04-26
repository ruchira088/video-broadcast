package exceptions

case class ExistingUsernameException(username: String) extends Exception {
  override def getMessage: String = s"Username already exist: $username"
}

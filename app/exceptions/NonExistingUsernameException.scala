package exceptions

case class NonExistingUsernameException(username: String) extends Exception {
  override def getMessage: String = s"Non existing username: $username"
}

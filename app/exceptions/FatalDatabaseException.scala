package exceptions

case object FatalDatabaseException extends Exception {
  override def getMessage: String = "Fatal database exception occurred"
}

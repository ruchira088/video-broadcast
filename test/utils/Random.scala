package utils

import com.github.javafaker.Faker
import web.requests.models.BroadcasterSignInRequest

trait Random[+A] {
  self =>

  def map[B](f: A => B): Random[B] = () => f(generate())

  def flatMap[B](f: A => Random[B]): Random[B] = () => f(self.generate()).generate()

  def generate(): A
}

object Random {
  val faker: Faker = Faker.instance()

  import faker._

  def apply[A](implicit random: Random[A]): A = random.generate()

  def gen[A](value: => A): Random[A] = () => value

  implicit val randomInt: Random[Int] = gen(math.abs(util.Random.nextInt()))

  implicit val randomBoolean: Random[Boolean] = randomInt.map(_ % 2 == 0)
  
  implicit def randomOption[A](implicit random: Random[A]): Random[Option[A]] =
    randomBoolean.map(booleanValue => if (booleanValue) Some(random.generate()) else None)

  def combine[A](random: Random[A]*): Random[A] =
    randomInt.flatMap { index => random(index % random.length) }

  val username: Random[String] = gen(name().username())

  val description: Random[String] =
    combine(
      gen(rickAndMorty().quote()),
      gen(chuckNorris().fact()),
      gen(gameOfThrones().quote()),
      gen(shakespeare().asYouLikeItQuote()),
      gen(yoda().quote())
    )

  implicit val randomBroadcasterSignInRequest: Random[BroadcasterSignInRequest] =
    for {
      broadcasterUsername <- username
      broadcasterDescription <- randomOption(description)
    }
    yield BroadcasterSignInRequest(broadcasterUsername, broadcasterDescription)
}

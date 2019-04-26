package dao.broadcaster

import java.sql.Timestamp

import config.SystemUtilities
import exceptions.{ExistingUsernameException, FatalDatabaseException}
import javax.inject.{Inject, Singleton}
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scalaz.OptionT
import scalaz.std.scalaFuture.futureInstance
import services.broadcast.models.Broadcaster
import slick.ast.BaseTypedType
import slick.jdbc.{JdbcProfile, JdbcType}
import slick.lifted.{PrimaryKey, ProvenShape}
import utils.MonadicUtils.OptionTWrapper

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{implicitConversions, postfixOps}

@Singleton
class SlickBroadcasterDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends BroadcasterDao
    with HasDatabaseConfigProvider[JdbcProfile] {

  import dbConfig.profile.api._

  implicit val jdbcProfile: JdbcProfile = dbConfig.profile

  import SlickBroadcasterDao._

  class BroadcasterTable(tag: Tag) extends Table[SlickBroadcaster](tag, TABLE_NAME) {
    def username: Rep[String] = column[String]("username")
    def description: Rep[Option[String]] = column[Option[String]]("description")
    def signedInAt: Rep[DateTime] = column[DateTime]("signed_in_at")
    def signedOutAt: Rep[DateTime] = column[DateTime]("signed_out_at")
    def pk: PrimaryKey = primaryKey("pk", (username, signedOutAt))

    override def * : ProvenShape[SlickBroadcaster] =
      (username, description, signedInAt, signedOutAt) <> (SlickBroadcaster.apply _ tupled, SlickBroadcaster.unapply)
  }

  val slickBroadcasters = TableQuery[BroadcasterTable]

  override def insert(broadcaster: Broadcaster)(implicit executionContext: ExecutionContext): Future[Broadcaster] =
    for {
      _ <- getByUsername(broadcaster.username) ifNotEmpty ExistingUsernameException(broadcaster.username)
      _ <- db.run(slickBroadcasters += SlickBroadcasterDao.toSlickBroadcaster(broadcaster))
      insertedBroadcaster <- getByUsername(broadcaster.username) ifEmpty FatalDatabaseException
    } yield insertedBroadcaster

  override def getByUsername(
    username: String
  )(implicit executionContext: ExecutionContext): OptionT[Future, Broadcaster] =
    OptionT {
      db.run {
        slickBroadcasters
            .filter { slickBroadcaster =>
              slickBroadcaster.username === username && slickBroadcaster.signedOutAt === NON_SIGNED_OUT_AT_PLACEHOLDER
            }
            .take(1)
            .result
        }
        .map {
          _.headOption.map(SlickBroadcasterDao.toBroadcaster)
        }
    }

  override def deleteByUsername(
    username: String
  )(implicit systemUtilities: SystemUtilities, executionContext: ExecutionContext): OptionT[Future, Broadcaster] =
    getByUsername(username)
      .flatMapF { broadcaster =>
        val signedOutAt = systemUtilities.currentTime()

        db.run {
          slickBroadcasters
              .filter { slickBroadcaster =>
                slickBroadcaster.username === username &&
                slickBroadcaster.signedOutAt === NON_SIGNED_OUT_AT_PLACEHOLDER
              }
              .map(_.signedOutAt)
              .update(signedOutAt)
          }
          .map(_ => broadcaster.copy(signedOutAt = Some(signedOutAt)))
      }
}

object SlickBroadcasterDao {
  val TABLE_NAME = "broadcasters"
  val NON_SIGNED_OUT_AT_PLACEHOLDER: DateTime = new DateTime(0)

  case class SlickBroadcaster(
    username: String,
    description: Option[String],
    signedInAt: DateTime,
    signedOutAt: DateTime
  )

  def toBroadcaster(slickBroadcaster: SlickBroadcaster): Broadcaster =
    Broadcaster(
      slickBroadcaster.username,
      slickBroadcaster.description,
      slickBroadcaster.signedInAt,
      if (slickBroadcaster.signedOutAt == NON_SIGNED_OUT_AT_PLACEHOLDER) None else Some(slickBroadcaster.signedOutAt)
    )

  def toSlickBroadcaster(broadcaster: Broadcaster): SlickBroadcaster =
    SlickBroadcaster(
      broadcaster.username,
      broadcaster.description,
      broadcaster.signedInAt,
      broadcaster.signedOutAt.getOrElse(NON_SIGNED_OUT_AT_PLACEHOLDER)
    )

  implicit def mappedDateTimeColumn(
    implicit jdbcProfile: JdbcProfile
  ): JdbcType[DateTime] with BaseTypedType[DateTime] = {
    import jdbcProfile.api._

    jdbcProfile.MappedColumnType.base[DateTime, Timestamp](
      dateTime => new Timestamp(dateTime.getMillis),
      timestamp => new DateTime(timestamp.getTime)
    )
  }
}

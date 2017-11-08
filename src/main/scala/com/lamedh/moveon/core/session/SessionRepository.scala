package com.lamedh.moveon.core.session

import com.lamedh.common.core.{ ErrorToken, InputError, Repository }

trait SessionRepository extends Repository[Session, String] {
  def alreadyExistsError(session: Session) =
    ErrorToken(s"Session with code ${session.allocationId} already exists", InputError)
}

package com.lamedh.moveon.infra.repo.inmemory

import com.lamedh.common.repo.inmemory.InMemoryRepo
import com.lamedh.moveon.core.movie.{Movie, MovieRepository}
import com.lamedh.moveon.core.session.{Session, SessionRepository}

class MovieInMemoryRepo extends InMemoryRepo[Movie, String] with MovieRepository
class SessionInMemoryRepo extends InMemoryRepo[Session, String] with SessionRepository

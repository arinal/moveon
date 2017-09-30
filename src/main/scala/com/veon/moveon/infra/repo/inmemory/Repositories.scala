package com.veon.moveon.infra.repo.inmemory

import com.veon.common.repo.inmemory.InMemoryRepo
import com.veon.moveon.core.movie.{Movie, MovieRepository}
import com.veon.moveon.core.session.{Session, SessionRepository}

class MovieInMemoryRepo extends InMemoryRepo[Movie, String] with MovieRepository
class SessionInMemoryRepo extends InMemoryRepo[Session, String] with SessionRepository

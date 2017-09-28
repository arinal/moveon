package com.veon.moveon.infra.repo.inmemory

import com.veon.common.infra.repo.inmemory.InMemoryRepo
import com.veon.moveon.core.movie.{Movie, MovieRepository}
import com.veon.moveon.core.session.{Session, SessionRepository}

class MovieInMemoryRepo extends MovieRepository with InMemoryRepo[Movie, String]
class SessionInMemoryRepo extends SessionRepository with InMemoryRepo[Session, String]

package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface SessionUserRepository: JpaRepository<SessionUserModel, UUID>
package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRefreshTokenRepository: JpaRepository<UserRefreshTokenModel, UUID>
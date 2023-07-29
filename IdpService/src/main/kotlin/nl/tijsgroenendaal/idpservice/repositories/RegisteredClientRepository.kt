package nl.tijsgroenendaal.idpservice.repositories

import nl.tijsgroenendaal.idpservice.entity.RegisteredClient
import org.springframework.data.jpa.repository.JpaRepository

interface RegisteredClientRepository: JpaRepository<RegisteredClient, String>
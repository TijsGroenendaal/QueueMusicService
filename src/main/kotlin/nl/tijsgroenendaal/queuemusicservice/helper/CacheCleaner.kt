package nl.tijsgroenendaal.queuemusicservice.helper

import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CacheCleaner {

    @CacheEvict(value = ["credentials-access-token"], allEntries = true)
    @Scheduled(fixedRateString = "\${queuemusic.cachecleaner.timer}")
    fun emptyHotelsCache() = Unit

}
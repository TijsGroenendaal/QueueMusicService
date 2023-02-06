package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration

import feign.codec.Encoder
import feign.form.FormEncoder

import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

class FormEncodedConfiguration {

    @Bean
    @Primary
    fun formEncoded(
        converter: ObjectFactory<HttpMessageConverters>
    ): Encoder {
        return FormEncoder(SpringEncoder(converter))
    }

}
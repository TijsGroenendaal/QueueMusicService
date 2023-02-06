package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration

import feign.codec.Encoder
import feign.form.FormEncoder

import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean

class FormEncodedConfiguration {

    @Bean
    fun formEncoded(
        converter: ObjectFactory<HttpMessageConverters>
    ): Encoder {
        return FormEncoder(SpringEncoder(converter))
    }

}
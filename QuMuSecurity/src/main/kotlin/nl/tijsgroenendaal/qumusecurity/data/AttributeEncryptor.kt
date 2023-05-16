package nl.tijsgroenendaal.qumusecurity.data

import org.springframework.beans.factory.annotation.Value

import jakarta.persistence.AttributeConverter
import java.util.Base64

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AttributeEncryptor(
    @Value("\${queuemusic.encryption.algorithme}")
    private val algorithme: String,
    @Value("\${queuemusic.encryption.secret}")
    private val secret: String
): AttributeConverter<String, String>{

    private val cipher = Cipher.getInstance(algorithme)

    override fun convertToDatabaseColumn(attibute: String?): String? {
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secret.toByteArray(), algorithme))
        if (attibute == null) return null
        return String(Base64.getEncoder().encode(cipher.doFinal(attibute.toByteArray())))
    }

    override fun convertToEntityAttribute(attibute: String?): String? {
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secret.toByteArray(), algorithme))
        if (attibute == null) return null
        return String(cipher.doFinal(Base64.getDecoder().decode(attibute.toByteArray())))
    }
}
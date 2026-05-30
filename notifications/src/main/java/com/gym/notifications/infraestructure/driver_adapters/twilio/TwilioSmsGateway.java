package com.gym.notifications.infraestructure.driver_adapters.twilio;

import com.gym.notifications.domain.model.gateway.SmsGateway;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementación del puerto SmsGateway usando la API de Twilio.
 * Envía SMS reales a través del servicio de Twilio.
 */
@Component
public class TwilioSmsGateway implements SmsGateway {

    private static final Logger log = LoggerFactory.getLogger(TwilioSmsGateway.class);

    private final TwilioProperties properties;

    public TwilioSmsGateway(TwilioProperties properties) {
        this.properties = properties;
    }

    /**
     * Inicializa el cliente de Twilio con las credenciales de la aplicación.
     * Se ejecuta una sola vez al arrancar el contexto de Spring.
     */
    @PostConstruct
    public void init() {
        Twilio.init(properties.getAccountSid(), properties.getAuthToken());
        log.info("Twilio client inicializado correctamente para la cuenta: {}",
                properties.getAccountSid());
    }

    /**
     * Envía un SMS real usando la API de Twilio.
     *
     * @param toPhoneNumber número destino en formato E.164 (ej: +573001234567)
     * @param messageBody   texto del SMS
     * @return SID del mensaje asignado por Twilio
     */
    @Override
    public String sendSms(String toPhoneNumber, String messageBody) {
        log.info("Enviando SMS a {} vía Twilio...", toPhoneNumber);

        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(properties.getPhoneNumber()),
                messageBody
        ).create();

        log.info("SMS enviado exitosamente. SID: {}, Estado: {}", message.getSid(), message.getStatus());
        return message.getSid();
    }
}

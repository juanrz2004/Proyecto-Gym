package com.gym.notifications.domain.model.gateway;

/**
 * Puerto de salida para el envío de SMS via API externa (Twilio).
 * La capa de dominio solo conoce esta interfaz, no la implementación concreta.
 */
public interface SmsGateway {
    /**
     * Envía un mensaje de texto SMS al número de teléfono indicado.
     *
     * @param toPhoneNumber número destino en formato E.164, ej: +573001234567
     * @param message       texto del mensaje
     * @return SID del mensaje generado por Twilio
     */
    String sendSms(String toPhoneNumber, String message);
}

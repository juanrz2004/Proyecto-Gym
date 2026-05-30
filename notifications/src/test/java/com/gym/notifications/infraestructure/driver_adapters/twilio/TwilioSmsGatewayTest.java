package com.gym.notifications.infraestructure.driver_adapters.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwilioSmsGatewayTest {

    private TwilioProperties properties;
    private TwilioSmsGateway gateway;

    @BeforeEach
    void setUp() {
        properties = new TwilioProperties();
        properties.setAccountSid("ACTEST00000000000000000000000000000");
        properties.setAuthToken("test_auth_token_00000000000000000");
        properties.setPhoneNumber("+10000000000");
        gateway = new TwilioSmsGateway(properties);
    }

    @Test
    void sendSms_validPhoneAndMessage_returnsSid() {
        Message mockMessage = mock(Message.class);
        when(mockMessage.getSid()).thenReturn("SM_TEST_SID_001");
        when(mockMessage.getStatus()).thenReturn(Message.Status.QUEUED);

        MessageCreator mockCreator = mock(MessageCreator.class);
        when(mockCreator.create()).thenReturn(mockMessage);

        try (MockedStatic<Twilio> twilioStatic = mockStatic(Twilio.class);
             MockedStatic<Message> messageStatic = mockStatic(Message.class)) {

            messageStatic.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(mockCreator);

            String sid = gateway.sendSms("+573001234567", "Hola desde el gimnasio");

            assertEquals("SM_TEST_SID_001", sid);
            messageStatic.verify(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq("Hola desde el gimnasio")
            ));
        }
    }

    @Test
    void sendSms_twilioThrowsException_propagatesRuntimeException() {
        try (MockedStatic<Twilio> twilioStatic = mockStatic(Twilio.class);
             MockedStatic<Message> messageStatic = mockStatic(Message.class)) {

            messageStatic.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenThrow(new RuntimeException("Twilio API error"));

            assertThrows(RuntimeException.class,
                    () -> gateway.sendSms("+573001234567", "Test message"));
        }
    }

    @Test
    void properties_gettersReturnCorrectValues() {
        assertEquals("ACTEST00000000000000000000000000000", properties.getAccountSid());
        assertEquals("test_auth_token_00000000000000000", properties.getAuthToken());
        assertEquals("+10000000000", properties.getPhoneNumber());
    }
}

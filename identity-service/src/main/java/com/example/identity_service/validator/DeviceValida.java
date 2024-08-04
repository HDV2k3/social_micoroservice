package com.example.identity_service.validator;

import com.example.identity_service.entity.DeviceMetadata;
import com.example.identity_service.entity.User;
import com.example.identity_service.repository.DeviceMetadataRepository;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.servlet.http.HttpServletRequest;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
@Component
public class DeviceValida {
    private static final String UNKNOWN = "UNKNOWN";

    private final DeviceMetadataRepository deviceMetadataRepository;
    private final DatabaseReader databaseReader;
    private final Parser parser;
    private final JavaMailSender mailSender;
    private final Environment env;



    public DeviceValida(DeviceMetadataRepository deviceMetadataRepository,
                        @Qualifier("GeoIPCity") DatabaseReader databaseReader,
                        Parser parser,
                        JavaMailSender mailSender,
                        Environment env
    ) {
        this.deviceMetadataRepository = deviceMetadataRepository;
        this.databaseReader = databaseReader;
        this.parser = parser;
        this.mailSender = mailSender;
        this.env = env;
    }
    public void verifyDevice(UserDetails user, User user1, String ipAddress, String userAgent) throws IOException, GeoIp2Exception {
        String location = getIpLocation(ipAddress);
        String deviceDetails = getDeviceDetails(userAgent);

        DeviceMetadata existingDevice = findExistingDevice((String) user1.getId(), deviceDetails, location);

        if (Objects.isNull(existingDevice)) {
            unknownDeviceNotification(deviceDetails, location, ipAddress, user1.getEmail());

            DeviceMetadata deviceMetadata = new DeviceMetadata();
            deviceMetadata.setUserId((String) user1.getId());
            deviceMetadata.setLocation(location);
            deviceMetadata.setDeviceDetails(deviceDetails);
            deviceMetadata.setLastLoggedIn(new Date());
            deviceMetadataRepository.save(deviceMetadata);
        } else {
            existingDevice.setLastLoggedIn(new Date());
            deviceMetadataRepository.save(existingDevice);
        }
    }

    private String extractIp(HttpServletRequest request) {
        String clientIp;
        String clientXForwardedForIp = request.getHeader("x-forwarded-for");
        if (nonNull(clientXForwardedForIp)) {
            clientIp = parseXForwardedHeader(clientXForwardedForIp);
        } else {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    private String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }

    private String getDeviceDetails(String userAgent) {
        String deviceDetails = UNKNOWN;

        Client client = parser.parse(userAgent);
        if (Objects.nonNull(client)) {
            deviceDetails = client.userAgent.family + " " + client.userAgent.major + "." + client.userAgent.minor +
                    " - " + client.os.family + " " + client.os.major + "." + client.os.minor;
        }

        return deviceDetails;
    }

    private String getIpLocation(String ip) throws IOException, GeoIp2Exception {

        String location = UNKNOWN;

        InetAddress ipAddress = InetAddress.getByName(ip);

        CityResponse cityResponse = databaseReader.city(ipAddress);
        if (Objects.nonNull(cityResponse) &&
                Objects.nonNull(cityResponse.getCity()) &&
                !Strings.isNullOrEmpty(cityResponse.getCity().getName())) {

            location = cityResponse.getCity().getName();
        }

        return location;
    }

    private DeviceMetadata findExistingDevice(String id, String deviceDetails, String location) {

        List<DeviceMetadata> knownDevices = deviceMetadataRepository.findByUserId(id);

        for (DeviceMetadata existingDevice : knownDevices) {
            if (existingDevice.getDeviceDetails().equals(deviceDetails) &&
                    existingDevice.getLocation().equals(location)) {
                return existingDevice;
            }
        }

        return null;
    }
    private void unknownDeviceNotification(String deviceDetails, String location, String ip, String email) {
        final String subject = "Security Alert: New Login Detected on Your Account";
        final SimpleMailMessage notification = new SimpleMailMessage();
        notification.setTo(email);
        notification.setSubject(subject);

        String text = String.format(
                "Dear Valued User,\n\n" +
                        "We have detected a new login to your account from an unfamiliar device. " +
                        "Your security is our top priority, so we wanted to notify you immediately.\n\n" +
                        "Login Details:\n" +
                        "• Device: %s\n" +
                        "• Location: %s\n" +
                        "• IP Address: %s\n" +
                        "• Time: %s\n\n" +
                        "If this was you, no further action is required. You can disregard this email.\n\n" +
                        "However, if you did not initiate this login, please take the following steps immediately:\n" +
                        "1. Change your password\n" +
                        "3. Review your recent account activity\n" +
                        "4. Contact our support team at support@example.com\n\n" +
                        "We're here to help ensure the security of your account. If you have any questions or concerns, " +
                        "please don't hesitate to reach out to our dedicated support team.\n\n" +
                        "Stay safe online!\n\n" +
                        "Best regards,\n" +
                        "Note: This is an automated message. Please do not reply directly to this email.",
                deviceDetails,
                location,
                ip,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        notification.setText(text);
        String from = "dacviethuynh@gmail.com";
        notification.setFrom(from);
        mailSender.send(notification);
    }

    private String getMessage(String code) {
        try {
            return env.getProperty(code);

        } catch (NoSuchMessageException ex) {
            return env.getProperty(code);
        }
    }
}

package ru.practicum;

import java.util.Optional;

public enum RequestStatus {

    PENDING, CANCELED, CONFIRMED, REJECTED;

    public static Optional<RequestStatus> from(String stringStatus) {
        for (RequestStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}

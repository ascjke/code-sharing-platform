package ru.borisov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Component
@Data
@Entity
public class Code {

    private static final String DATE_FORMATTER = "yyyy/MM/dd HH:mm:ss";

    @Id
    @Column(name = "code_id")
    @JsonIgnore
    private String id;

    @Column
    @NotNull
    @NotBlank(message = "Code snippet is mandatory")
    private String code;

    @Column
    private String date;

    @Column
    private long time;

    @Column
    private int views;

    @JsonIgnore
    @Column
    private LocalDateTime creationDateTime;

    @JsonIgnore
    @Column
    @Transient
    private UUID uuid;

    @JsonIgnore
    @Column
    private LocalDateTime expirationDateTime;

    @JsonIgnore
    @Column
    private boolean timeRestricted;

    @JsonIgnore
    @Column
    private boolean viewsRestricted;

    public void setDate() {
        creationDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        date = creationDateTime.format(formatter);
    }

    public void generateId() {
        uuid = UUID.randomUUID();
        id = uuid.toString();
    }

    public void setExpirationTime() {
        expirationDateTime = creationDateTime.plusSeconds(time);
    }

    public void updateTime() {
        time = ChronoUnit.SECONDS.between(LocalDateTime.now(), expirationDateTime);
    }

    public boolean shouldBeDeleted() {
        return time < 0 || views < 0;
    }
}
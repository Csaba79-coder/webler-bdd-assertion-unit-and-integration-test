package hu.webler.weblerbddassertionunitandintegrationtest.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.fromString;

@MappedSuperclass
@Getter
public class Auditable extends Identifier {

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt = LocalDate.now();

    @Column(name = "created_by", nullable = false)
    private UUID createdBy = fromString("b37c1f7f-cd48-496d-a5ba-77863b33aa3e");

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy = fromString("b37c1f7f-cd48-496d-a5ba-77863b33aa3e");
}

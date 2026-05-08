package ru.unlegit.cdn.contentmetadata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content_metadata")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ContentMetadata {

    @Id
    @Column(name = "content_id")
    String contentId;
    @Column(name = "display_name", nullable = false, length = 64)
    String displayName;
}
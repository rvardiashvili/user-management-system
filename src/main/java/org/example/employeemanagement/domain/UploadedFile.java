package org.example.employeemanagement.domain;


import jakarta.persistence.*;
import lombok.*;
import org.example.employeemanagement.domain.people.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_files")
@Getter
@Setter
@NoArgsConstructor
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="original_file_name")
    private String fileName;
    @Column(name="content_type")
    private String fileType;
    @Column(name="file_size_bytes")
    private Long fileSize;
    @Column(name="bucket_name")
    private String bucketName;
    @Column(name="object_name")
    private String objectName;
    @Column(name="upload_timestamp")
    private LocalDateTime uploadedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

}

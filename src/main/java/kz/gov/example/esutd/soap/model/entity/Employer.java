package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employers")
@NoArgsConstructor
@Data
public class Employer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "employer_id", nullable = false, updatable = false)
    private String employerId; 

    @Column(name = "bin", nullable = false, unique = true, length = 12)
    private String bin;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "kato_code")
    private String katoCode;

    @Column(name = "employer_type")
    private String employerType;

    @Column(name = "address_country", nullable = false)
    private String addressCountry;

    @Column(name = "address_kato_code")
    private String addressKatoCode;

    @Column(name = "address_city")
    private String addressCity;

    @Column(name = "address_street")
    private String addressStreet;

    @Column(name = "address_building")
    private String addressBuilding;

    @Column(name = "address_postal_code")
    private String addressPostalCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bik")
    private String bik;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}
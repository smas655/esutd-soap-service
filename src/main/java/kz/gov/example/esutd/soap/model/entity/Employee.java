package kz.gov.example.esutd.soap.model.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@Data
public class Employee {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "employee_id", nullable = false, updatable = false)
    private String employeeId;

    @Column(name = "iin", nullable = false, unique = true, length = 12)
    private String iin;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "citizenship", nullable = false)
    private String citizenship;

    @Column(name = "address_country")
    private String addressCountry;

    @Column(name = "address_kato_code")
    private String addressKatoCode;
    
    @Column(name = "address_city")
    private String addressCity;
    
    @Column(name = "address_street")
    private String addressStreet;
    
    @Column(name = "address_building")
    private String addressBuilding;
    
    @Column(name = "address_apartment")
    private String addressApartment;
    
    @Column(name = "address_postal_code")
    private String addressPostalCode;
    
    @Column(name = "bank_name")
    private String bankName;
    
    @Column(name = "bik")
    private String bik;
    
    @Column(name = "account_number")
    private String accountNumber;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "hr_phone_number")
    private String hrPhoneNumber;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public enum Gender {
        MALE, FEMALE
    }
}

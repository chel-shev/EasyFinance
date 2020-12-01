package ru.ixec.easyfinance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue
    private Long clientId;
    private String username;
    private String email;
    private Long chatId;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<AccountEntity> accountList;

    @OneToMany(mappedBy = "client")
    private List<InquiryEntity> inquiryList;
}

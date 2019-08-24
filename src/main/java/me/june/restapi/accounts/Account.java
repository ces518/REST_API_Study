package me.june.restapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: june
 * Date: 2019-08-24
 * Time: 20:59
 **/
@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    /* 기본값이 Lazy 이지만 매번 권한이 필요하기때문에 EAGER */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}

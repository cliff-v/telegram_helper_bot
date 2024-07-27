package com.safronov.timofei.telegram.helper.bot.model.db;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class UserDao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    protected Long id;

    private String tgName;
    private String comment;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private String realName;
    private Long tgId;
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "ai_model_id")
    private AiModel aiModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_service_title")
    private ServiceItem lastServiceItem;
}
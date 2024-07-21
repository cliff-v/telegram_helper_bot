package com.safronov.timofei.telegram.helper.bot.model.db;

import com.safronov.timofei.telegram.helper.bot.model.BaseSequenceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserDao extends BaseSequenceEntity {
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
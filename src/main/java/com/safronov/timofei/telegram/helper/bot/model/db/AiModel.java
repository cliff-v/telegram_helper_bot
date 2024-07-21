package com.safronov.timofei.telegram.helper.bot.model.db;

import com.safronov.timofei.telegram.helper.bot.model.BaseSequenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ai_models")
public class AiModel extends BaseSequenceEntity {
    private String model;
    private Integer tokens;
}

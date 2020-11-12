package com.levik.hibernate.spring.api.dto;

import com.levik.hibernate.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {

    private List<Player> players;
}

package com.rigandbarter.componentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "MotherboardComponent")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MotherboardComponent extends Component {
    private String connector;
    private String socket;
    private String memoryType;
    private int memoryCapacity;
    private int memorySlots;
}

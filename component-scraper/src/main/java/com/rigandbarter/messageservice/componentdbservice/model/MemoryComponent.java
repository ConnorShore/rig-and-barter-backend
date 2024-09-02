package com.rigandbarter.messageservice.componentdbservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MemoryComponent extends Component {
    private String type;
    private int size;
    private int clockSpeed;
    private int numSticks;
}

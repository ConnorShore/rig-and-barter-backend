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
public class VideoCardComponent extends Component {
    private double length;
    private double slots;
    private int numHDMIs;
    private int numDisplayPorts;
    private int boostClock;
    private int vram;
}

package com.rigandbarter.componentscraper.model;

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
public class HardDriveComponent extends Component {
    private int size;   // in gb
    private double rpm;
    private int cacheSize;
}

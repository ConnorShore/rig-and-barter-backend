package com.rigandbarter.componentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "SolidStateDriveComponent")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SolidStateDriveComponent extends Component {
    private String formFactor;
    private String protocol;
    private int size;       //in gb
}

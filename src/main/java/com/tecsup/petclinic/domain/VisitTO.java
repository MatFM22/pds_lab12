package com.tecsup.petclinic.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer Object for Visit.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitTO {

    private Integer id;
    private Integer petId;
    private String visitDate;
    private String description;
}

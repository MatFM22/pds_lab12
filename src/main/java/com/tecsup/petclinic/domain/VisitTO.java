package com.tecsup.petclinic.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class VisitTO {

    private Integer id;

    private Integer petId;

    private String visitDate;

    private String description;


}
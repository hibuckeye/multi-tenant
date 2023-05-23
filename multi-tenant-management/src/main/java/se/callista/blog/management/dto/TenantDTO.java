package se.callista.blog.management.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TenantDTO {

    @NotNull
    private String name;

}

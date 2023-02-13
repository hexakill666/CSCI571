package com.hw9.test.demo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Results {
    public String type;
    public String title;
    public String description;
    public String og_type;
    public Links _links;

}

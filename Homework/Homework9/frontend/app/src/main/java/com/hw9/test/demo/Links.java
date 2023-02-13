package com.hw9.test.demo;

import com.hw9.test.demo.Self.Self;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Links {
    public Self self;
    public Permalink permalink;
    public Thumbnail thumbnail;
}

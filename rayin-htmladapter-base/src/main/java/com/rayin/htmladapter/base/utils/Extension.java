package com.rayin.htmladapter.base.utils;

import lombok.Getter;
import lombok.Setter;

public class Extension {

    @Getter @Setter
    private String oid;

    @Getter @Setter
    private boolean critical;

    @Getter @Setter
    private byte[] value;
}

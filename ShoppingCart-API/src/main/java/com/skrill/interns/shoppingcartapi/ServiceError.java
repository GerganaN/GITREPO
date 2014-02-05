/*
 * $ Gergana Nikolova, Veselin Aleksandrov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2014> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.shoppingcartapi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ServiceError")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceError {
    @XmlElement
    private String errorMessage;

    public ServiceError() {

    }

    public ServiceError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

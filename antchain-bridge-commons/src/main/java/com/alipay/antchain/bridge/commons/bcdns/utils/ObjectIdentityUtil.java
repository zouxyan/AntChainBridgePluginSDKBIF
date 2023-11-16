/*
 * Copyright 2023 Ant Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alipay.antchain.bridge.commons.bcdns.utils;

import java.security.PublicKey;

import com.alipay.antchain.bridge.commons.core.base.BIDInfoObjectIdentity;
import com.alipay.antchain.bridge.commons.core.base.ObjectIdentity;
import com.alipay.antchain.bridge.commons.core.base.X509PubkeyInfoObjectIdentity;
import com.alipay.antchain.bridge.commons.exception.AntChainBridgeCommonsException;
import com.alipay.antchain.bridge.commons.exception.CommonsErrorCodeEnum;

public class ObjectIdentityUtil {

    public static PublicKey getPublicKeyFromSubject(ObjectIdentity oid, byte[] rawSubject) {
        switch (oid.getType()) {
            case X509_PUBLIC_KEY_INFO:
                return new X509PubkeyInfoObjectIdentity(oid).getPublicKey();
            case BID:
                return new BIDInfoObjectIdentity(oid).getPublicKey();
            default:
                throw new AntChainBridgeCommonsException(
                        CommonsErrorCodeEnum.BCDNS_OID_UNSUPPORTED_TYPE,
                        "unsupported oid type" + oid.getType().name()
                );
        }
    }

    public static byte[] getRawPublicKeyFromSubject(ObjectIdentity oid, byte[] rawSubject) {
        switch (oid.getType()) {
            case X509_PUBLIC_KEY_INFO:
                return new X509PubkeyInfoObjectIdentity(oid).getRawPublicKey();
            case BID:
                return new BIDInfoObjectIdentity(oid).getRawPublicKey();
            default:
                throw new AntChainBridgeCommonsException(
                        CommonsErrorCodeEnum.BCDNS_OID_UNSUPPORTED_TYPE,
                        "unsupported oid type" + oid.getType().name()
                );
        }
    }
}

/*
 * @(#)AddressesTest.java     22 Sep 2011
 *
 * Copyright © 2010 Andrew Phillips.
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package ext.deployit.community.plugin.notifications.email.util;

import static ext.deployit.community.plugin.notifications.email.util.Addresses.NameAndAddress.toNameAndAddress;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ext.deployit.community.plugin.notifications.email.util.Addresses.NameAndAddress;

/**
 * Unit tests for {@link Addresses)
 */
public class NameAndAddressTest {

    @Test
    public void handlesNameAndEmail() {
        NameAndAddress nameAndAddress = toNameAndAddress("James Bond <jbond@mi6.gov.uk>");
        assertEquals("James Bond", nameAndAddress.getName());
        assertEquals("jbond@mi6.gov.uk", nameAndAddress.getAddress());
    }
    
    @Test
    public void handlesEmailOnly() {
        NameAndAddress nameAndAddress = toNameAndAddress("jbond@mi6.gov.uk");
        assertEquals("jbond@mi6.gov.uk", nameAndAddress.getAddress());
    }
}

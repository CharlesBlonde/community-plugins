/*
 * @(#)ConfigParserTest.java     Jul 31, 2011
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
package ext.deployit.community.importer.jeearchive.scanner;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.jar.Attributes.Name;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import ext.deployit.community.importer.jeearchive.scanner.ManifestScanner.ConfigParser;

/**
 * Unit tests for the {@link ManifestScanner.ConfigParser}
 */
public class ManifestScannerConfigParserTest {

    @Test(expected = IllegalArgumentException.class)
    public void failsIfRequiredPropertiesAreMissing() {
        new ConfigParser().apply(ImmutableMap.<String, String>of());
    }
    
    @Test
    public void returnsManifestScanner() throws Exception {
        ManifestScanner scanner = new ConfigParser().apply(
                ImmutableMap.of("nameManifestAttribute", "App-Name",
                        "versionManifestAttribute", "App-Version"));
        assertScannerField(scanner, "nameAttribute", new Name("App-Name"));
        assertScannerField(scanner, "versionAttribute", new Name("App-Version"));
    }

	private void assertScannerField(ManifestScanner scanner, String attribute, Name name)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = ManifestScanner.class.getDeclaredField(attribute);
		field.setAccessible(true);
        assertEquals(name, field.get(scanner));
	}
}
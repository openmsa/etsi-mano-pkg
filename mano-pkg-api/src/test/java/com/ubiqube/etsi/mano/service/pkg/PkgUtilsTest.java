/**
 *     Copyright (C) 2019-2023 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 *
 * @author Olivier Vignaud
 *
 */
class PkgUtilsTest {

	@Test
	void test() {
		final InputStream is = InputStream.nullInputStream();
		PkgUtils.fetchData(is);
		assertTrue(true);
	}

	@Test
	void test1() throws IOException {
		final InputStream is = Mockito.mock(InputStream.class);
		when(is.transferTo(any())).thenThrow(IOException.class);
		assertThrows(ToscaException.class, () -> PkgUtils.fetchData(is));
	}

}

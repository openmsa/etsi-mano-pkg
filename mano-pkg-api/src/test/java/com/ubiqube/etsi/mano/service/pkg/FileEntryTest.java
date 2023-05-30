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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 *
 * @author Olivier Vignaud
 *
 */
class FileEntryTest {

	@Test
	void test() {
		final FileEntry srv = new FileEntry(null, null);
		srv.content();
		srv.fileName();
		srv.hashCode();
		srv.equals(srv);
		srv.equals("");
		srv.equals(null);
		final FileEntry srv2 = new FileEntry(null, null);
		srv.equals(srv2);
		final FileEntry srv3 = new FileEntry("", null);
		srv.equals(srv3);
		final FileEntry srv4 = new FileEntry(null, "hello".getBytes());
		srv.equals(srv4);
		assertNotNull(srv4.toString());
	}

}

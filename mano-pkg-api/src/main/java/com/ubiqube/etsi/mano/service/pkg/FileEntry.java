/**
 *     Copyright (C) 2019-2020 Ubiqube.
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

import java.util.Arrays;
import java.util.Objects;

/**
 * Used to store small content.
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
public record FileEntry(String fileName, byte[] content) {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(content);
		return (prime * result) + Objects.hash(fileName);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		final FileEntry other = (FileEntry) obj;
		return Arrays.equals(content, other.content) && Objects.equals(fileName, other.fileName);
	}

	@Override
	public String toString() {
		return "FileEntry [fileName=" + fileName + ", content=" + Arrays.toString(content) + "]";
	}

}

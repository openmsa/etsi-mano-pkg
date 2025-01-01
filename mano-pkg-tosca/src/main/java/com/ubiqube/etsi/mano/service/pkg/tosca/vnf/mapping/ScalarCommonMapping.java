/**
 * Copyright (C) 2019-2025 Ubiqube.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import org.mapstruct.Mapper;

import com.ubiqube.parser.tosca.scalar.Frequency;
import com.ubiqube.parser.tosca.scalar.Size;
import com.ubiqube.parser.tosca.scalar.Time;

@Mapper
public interface ScalarCommonMapping {

	default Long toLong(final Time time) {
		if (null == time) {
			return null;
		}
		return time.getValue().longValue();
	}

	default long map(final Size value) {
		if (null == value) {
			return 0;
		}
		return value.getValue().longValue();
	}

	default Double toDouble(final Frequency frequency) {
		if (frequency == null) {
			return null;
		}
		return frequency.getValue();
	}

}

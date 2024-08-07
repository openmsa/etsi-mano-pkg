/**
 *     Copyright (C) 2019-2024 Ubiqube.
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
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiqube.etsi.mano.dao.mano.TriggerDefinition;
import com.ubiqube.parser.tosca.ParseException;

@Mapper
public interface TriggerDefinitionMapping {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "action", qualifiedByName = "toJsonString")
	@Mapping(target = "condition", qualifiedByName = "toJsonString")
	TriggerDefinition mapToTriggerDefinition(com.ubiqube.parser.tosca.TriggerDefinition o);

	@Named("toJsonString")
	default String toJsonString(final Object o) {
		if (o instanceof final String s) {
			return s;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(o);
		} catch (final JsonProcessingException e) {
			throw new ParseException("Unable to parse condition: " + o, e);
		}
	}

}

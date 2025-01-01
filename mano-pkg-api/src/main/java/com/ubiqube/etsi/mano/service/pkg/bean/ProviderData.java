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
package com.ubiqube.etsi.mano.service.pkg.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ubiqube.etsi.mano.dao.mano.Attributes;
import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.etsi.mano.dao.mano.pkg.VnfProfile;
import com.ubiqube.parser.tosca.AttributeAssignement;
import com.ubiqube.parser.tosca.InterfaceDefinition;
import com.ubiqube.parser.tosca.RequirementDefinition;
import com.ubiqube.parser.tosca.objects.tosca.interfaces.nfv.Vnflcm;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
@Getter
@Setter
public class ProviderData {

	private String vnfProvider;

	private String vnfProductName;
	/**
	 * Human readable description of the VNF Product
	 */
	private String productInfoDescription;

	private String vnfSoftwareVersion;

	private String vnfVersion;

	private String flavorId;
	/**
	 * Human readable description of the DF
	 */
	@NotNull
	private String flavourDescription;

	private String vnfdVersion;

	private String descriptorId;

	private String descriptorVersion;

	private Set<MonitoringParams> monitoringParameters;

	private Set<String> vnfmInfo;

	private VnfProfile vnfProfile;

	/**
	 * Default localization language that is instantiated if no information about
	 * selected localization language is available
	 */
	private String defaultLocalizationLanguage;
	/**
	 * Information about localization languages of the VNF
	 */
	private Set<String> localizationLanguages;
	/**
	 * Scale status of the VNF, one entry per aspect. Represents for every scaling
	 * aspect how "big" the VNF has been scaled w.r.t. that aspect.
	 */
	@NotNull
	private Set<ScaleInfo> scaleStatus;

	private String virtualLinkReq;
	private String virtualLink1Req;
	private String virtualLink2Req;
	private String virtualLink3Req;
	private String virtualLink4Req;
	private String virtualLink5Req;
	private String virtualLink6Req;
	private String virtualLink7Req;
	private String virtualLink8Req;
	private String virtualLink9Req;
	private String virtualLink10Req;

	private Vnflcm Vnflcm;

	private List<Attributes> attributes = new ArrayList<>();

	// See Attributes for overloadedAttributes
	private Map<String, AttributeAssignement> overloadedAttributes = new HashMap<>();

	private RequirementDefinition overloadedRequirements;

	private Map<String, InterfaceDefinition> overloadedInterfaces = new HashMap<>();

}

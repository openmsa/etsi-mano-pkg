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
package com.ubiqube.etsi.mano.service.pkg.tosca.ns;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ubiqube.etsi.mano.repository.ManoResource;
import com.ubiqube.etsi.mano.repository.NsdRepository;
import com.ubiqube.etsi.mano.repository.VirtualFileSystem;
import com.ubiqube.etsi.mano.service.pkg.PackageDescriptor;
import com.ubiqube.etsi.mano.service.pkg.ns.NsPackageProvider;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.Sol004PreOnboarding;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsInformationsMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsSapMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVirtualLinkMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.NsVnfIndicatorMapping;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.PkgMapper;
import com.ubiqube.etsi.mano.sol004.CsarModeEnum;
import com.ubiqube.etsi.mano.sol004.Sol004Onboarding;

import org.jspecify.annotations.NonNull;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
@Service
public class ToscaNsRegistryHandler implements PackageDescriptor<NsPackageProvider> {
	@NonNull
	private final NsdRepository repo;
	@NonNull
	private final PkgMapper mapper;
	@NonNull
	private final NsInformationsMapping nsInformationsMapping;
	@NonNull
	private final NsVnfIndicatorMapping nsVnfIndicatorMapping;
	@NonNull
	private final NsSapMapping nsSapMapping;
	@NonNull
	private final NsVirtualLinkMapping nsVirtualLinkMapping;

	private final ArtefactInformationsMapping artefactInformationsMapping;

	public ToscaNsRegistryHandler(final NsdRepository repo, final NsVirtualLinkMapping nsVirtualLinkMapping, final PkgMapper mapper, final NsVnfIndicatorMapping nsVnfIndicatorMapping, final NsSapMapping nsSapMapping, final NsInformationsMapping nsInformationsMapping, final ArtefactInformationsMapping artefactInformationsMapping) {
		this.repo = repo;
		this.mapper = mapper;
		this.nsInformationsMapping = nsInformationsMapping;
		this.nsVnfIndicatorMapping = nsVnfIndicatorMapping;
		this.nsSapMapping = nsSapMapping;
		this.nsVirtualLinkMapping = nsVirtualLinkMapping;
		this.artefactInformationsMapping = artefactInformationsMapping;
	}

	@Override
	public boolean isProcessable(final ManoResource data) {
		final Sol004PreOnboarding pre = new Sol004PreOnboarding(data);
		return pre.getMode() != CsarModeEnum.UNKNOWN;
	}

	@Override
	public String getProviderName() {
		return "TOSCA-NS";
	}

	@Override
	public NsPackageProvider getNewReaderInstance(final InputStream data, final UUID id) {
		return new ToscaNsPackageProvider(data, repo, id, mapper, nsInformationsMapping, nsVnfIndicatorMapping, nsSapMapping, nsVirtualLinkMapping, artefactInformationsMapping);
	}

	@Override
	public VirtualFileSystem getFileSystem(final ManoResource res) {
		final Sol004Onboarding pack = new Sol004Onboarding();
		return pack.getVirtualFileSystem(res);
	}

}

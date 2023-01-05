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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package com.ubiqube.etsi.mano.pkg.k8s;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ubiqube.etsi.mano.repository.ManoResource;
import com.ubiqube.etsi.mano.repository.VirtualFileSystem;
import com.ubiqube.etsi.mano.service.pkg.PackageDescriptor;
import com.ubiqube.etsi.mano.service.pkg.vnf.VnfPackageReader;
import com.ubiqube.etsi.mano.service.pkg.wfe.ExecutionGraph;

import io.kubernetes.client.openapi.models.V1Pod;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@Service
public class K8sPodRegistryHandler implements PackageDescriptor<VnfPackageReader> {

	private static final Logger LOG = LoggerFactory.getLogger(K8sPodRegistryHandler.class);

	public ExecutionGraph getBlueprint() {
		return null;
	}

	@Override
	public String getProviderName() {
		return "K8S-POD";
	}

	public boolean isProcessable(final byte[] data) {
		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		try {
			mapper.readValue(data, V1Pod.class);
			return true;
		} catch (final IOException e) {
			LOG.trace("", e);
		}
		return false;
	}

	public VnfPackageReader getNewReaderInstance(final byte[] data) {
		return new K8sPodReader(data);
	}

	@Override
	public boolean isProcessable(final ManoResource mr) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public VnfPackageReader getNewReaderInstance(final InputStream mr, final UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VirtualFileSystem getFileSystem(final ManoResource res) {
		// TODO Auto-generated method stub
		return null;
	}

}

// Copyright 2017 Sebastian Kuerten
//
// This file is part of OpenMetroMaps.
//
// OpenMetroMaps is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// OpenMetroMaps is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with OpenMetroMaps. If not, see <http://www.gnu.org/licenses/>.

package org.openmetromaps.maps;

import org.openmetromaps.maps.graph.LineNetwork;

public class MapView
{

	private String name;
	private LineNetwork lineNetwork;
	private ViewConfig config;

	public MapView(String name, LineNetwork lineNetwork, ViewConfig config)
	{
		this.name = name;
		this.lineNetwork = lineNetwork;
		this.config = config;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public LineNetwork getLineNetwork()
	{
		return lineNetwork;
	}

	public void setLineNetwork(LineNetwork lineNetwork)
	{
		this.lineNetwork = lineNetwork;
	}

	public ViewConfig getConfig()
	{
		return config;
	}

	public void setConfig(ViewConfig config)
	{
		this.config = config;
	}

}

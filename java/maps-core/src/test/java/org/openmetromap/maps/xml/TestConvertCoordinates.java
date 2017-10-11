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

package org.openmetromap.maps.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.openmetromaps.maps.xml.XmlModel;
import org.openmetromaps.maps.xml.XmlModelReader;
import org.openmetromaps.maps.xml.XmlStation;
import org.xml.sax.SAXException;

import de.topobyte.adt.geo.BBox;
import de.topobyte.adt.geo.BBoxHelper;
import de.topobyte.adt.geo.Coordinate;

public class TestConvertCoordinates
{

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException
	{
		InputStream input = TestConvertCoordinates.class.getClassLoader()
				.getResourceAsStream("berlin.xml");
		XmlModel model = XmlModelReader.read(input);

		List<Coordinate> coordinates = new ArrayList<>();
		for (XmlStation station : model.getStations()) {
			Coordinate location = station.getLocation();
			coordinates.add(location);
		}

		convert(coordinates);
	}

	private static void convert(List<Coordinate> coordinates)
	{
		BBox bbox = BBoxHelper.minimumBoundingBox(coordinates);
		System.out.println(bbox);
	}

}
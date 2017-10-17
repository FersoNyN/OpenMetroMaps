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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.openmetromaps.maps.CoordinateConversion;
import org.openmetromaps.maps.MapModel;
import org.openmetromaps.maps.xml.XmlModel;
import org.openmetromaps.maps.xml.XmlModelConverter;
import org.openmetromaps.maps.xml.XmlModelReader;
import org.openmetromaps.maps.xml.XmlModelWriter;
import org.xml.sax.SAXException;

public class TestConvert
{

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, TransformerException
	{
		InputStream input = TestConvert.class.getClassLoader()
				.getResourceAsStream("berlin.xml");

		XmlModel xmlModel = XmlModelReader.read(input);
		input.close();

		XmlModelConverter modelConverter = new XmlModelConverter();
		MapModel model = modelConverter.convert(xmlModel);

		CoordinateConversion.convertViews(model);

		XmlModelWriter writer = new XmlModelWriter();

		writer.write(System.out, model.getData(), model.getViews());
	}

}

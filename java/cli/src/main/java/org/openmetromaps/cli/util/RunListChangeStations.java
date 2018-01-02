// Copyright 2018 Sebastian Kuerten
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

package org.openmetromaps.cli.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.openmetromaps.maps.Edges;
import org.openmetromaps.maps.MapModel;
import org.openmetromaps.maps.graph.LineNetwork;
import org.openmetromaps.maps.graph.LineNetworkBuilder;
import org.openmetromaps.maps.graph.LineNetworkUtil;
import org.openmetromaps.maps.graph.NetworkLine;
import org.openmetromaps.maps.graph.Node;
import org.openmetromaps.maps.model.Line;
import org.openmetromaps.maps.model.Station;
import org.openmetromaps.maps.xml.DesktopXmlModelReader;
import org.openmetromaps.maps.xml.XmlModel;
import org.openmetromaps.maps.xml.XmlModelConverter;
import org.openmetromaps.misc.Context;
import org.openmetromaps.misc.Util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class RunListChangeStations
{

	private static final String OPTION_INPUT = "input";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_INPUT, true, true, "file", "an OpenMetroMaps model file");
			// @formatter:on
			return new CommonsCliExeOptions(options, "[options]");
		}

	};

	public static void main(String name, CommonsCliArguments arguments)
			throws Exception
	{
		CommandLine line = arguments.getLine();

		String argInput = line.getOptionValue(OPTION_INPUT);
		Path pathInput = Paths.get(argInput);

		System.out.println("Input: " + pathInput);

		InputStream input = Files.newInputStream(pathInput);

		XmlModel xmlModel = DesktopXmlModelReader.read(input);

		XmlModelConverter modelConverter = new XmlModelConverter();
		MapModel model = modelConverter.convert(xmlModel);

		execute(model);
	}

	private static void execute(MapModel model)
	{
		List<Edges> edges = new ArrayList<>();
		for (Line line : model.getData().lines) {
			edges.add(new Edges(line.getName()));
		}

		LineNetworkBuilder builder = new LineNetworkBuilder(model.getData(),
				edges);
		LineNetwork lineNetwork = builder.getGraph();

		Multimap<Station, Line> stationToLines = HashMultimap.create();
		Util.fillStationToLines(stationToLines, model);

		Context context = new Context(stationToLines, lineNetwork);

		Set<Node> changeNodes = new HashSet<>();

		List<NetworkLine> lines = lineNetwork.getLines();
		for (NetworkLine line : lines) {
			List<Node> nodes = LineNetworkUtil.getNodes(lineNetwork, line.line);
			for (Node node : nodes) {
				List<Line> changeLines = Util.determineInterestingLines(context,
						line, node);
				if (changeLines.isEmpty()) {
					continue;
				}

				changeNodes.add(node);
			}
		}

		List<Station> changeStations = new ArrayList<>();

		for (Node node : changeNodes) {
			changeStations.add(node.station);
		}

		Collections.sort(changeStations, new Comparator<Station>() {

			@Override
			public int compare(Station o1, Station o2)
			{
				return o1.getName().compareTo(o2.getName());
			}

		});

		for (Station station : changeStations) {
			System.out.println(station.getName());
		}
	}

}
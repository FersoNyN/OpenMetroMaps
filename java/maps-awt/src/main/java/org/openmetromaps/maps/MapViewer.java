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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.beans.PropertyChangeSupport;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.openmetromaps.maps.PlanRenderer.SegmentMode;
import org.openmetromaps.maps.PlanRenderer.StationMode;
import org.openmetromaps.maps.actions.AboutAction;
import org.openmetromaps.maps.actions.DebugRanksAction;
import org.openmetromaps.maps.actions.DebugTangentsAction;
import org.openmetromaps.maps.actions.ExitAction;
import org.openmetromaps.maps.actions.LicenseAction;
import org.openmetromaps.maps.actions.OpenAction;
import org.openmetromaps.maps.actions.SaveAction;
import org.openmetromaps.maps.actions.SaveAsAction;
import org.openmetromaps.maps.actions.ShowLabelsAction;
import org.openmetromaps.maps.graph.LineNetwork;
import org.openmetromaps.maps.graph.Node;

import de.topobyte.adt.geo.Coordinate;
import de.topobyte.awt.util.GridBagConstraintsEditor;
import de.topobyte.geomath.WGS84;
import de.topobyte.swing.util.EmptyIcon;
import de.topobyte.swing.util.action.enums.DefaultAppearance;
import de.topobyte.swing.util.action.enums.EnumActions;

public class MapViewer
{

	private MapModel model;
	private MapView view;

	private ViewConfig viewConfig;

	private JFrame frame;

	private ScrollableAdvancedPanel map;
	private StatusBar statusBar;

	public MapViewer(MapModel model)
	{
		init(model);
	}

	public void setModel(MapModel model)
	{
		init(model);
		map.setData(model.getData());
		map.setViewConfig(viewConfig.getBbox(), viewConfig.getStartPosition(),
				Constants.DEFAULT_ZOOM);
	}

	private void init(MapModel model)
	{
		this.model = model;

		view = model.getViews().get(0);
		LineNetwork lineNetwork = view.getLineNetwork();
		view = new MapView(view.getName(), lineNetwork);

		viewConfig = ModelUtil.viewConfig(model.getData());
	}

	public MapModel getModel()
	{
		return model;
	}

	public MapView getView()
	{
		return view;
	}

	public Window getFrame()
	{
		return frame;
	}

	public ScrollableAdvancedPanel getMap()
	{
		return map;
	}

	public StatusBar getStatusBar()
	{
		return statusBar;
	}

	public void show()
	{
		frame = new JFrame("Map Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);

		build();

		frame.setVisible(true);
	}

	private void build()
	{
		setupContent();
		setupMenu();

		MapViewerMouseEventProcessor mep = new MapViewerMouseEventProcessor(
				this);
		map.setMouseProcessor(mep);
	}

	private void setupMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);

		JMenu menuView = new JMenu("View");
		menuBar.add(menuView);

		JMenu menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);

		menuFile.add(new OpenAction(this));
		menuFile.add(new SaveAction(this));
		menuFile.add(new SaveAsAction(this));
		menuFile.add(new ExitAction());

		addCheckbox(menuView, new ShowLabelsAction(this));
		JMenu stationMode = submenu("Station mode");
		JMenu segmentMode = submenu("Segment mode");
		menuView.add(stationMode);
		menuView.add(segmentMode);
		addCheckbox(menuView, new DebugTangentsAction(this));
		addCheckbox(menuView, new DebugRanksAction(this));

		PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

		EnumActions.add(stationMode, StationMode.class, changeSupport,
				"station-mode", StationMode.CONVEX, x -> setStationMode(x),
				new DefaultAppearance<>());
		EnumActions.add(segmentMode, SegmentMode.class, changeSupport,
				"segment-mode", SegmentMode.CURVE, x -> setSegmentMode(x),
				new DefaultAppearance<>());

		menuHelp.add(new AboutAction(frame));
		menuHelp.add(new LicenseAction(frame));
	}

	private void setStationMode(StationMode mode)
	{
		map.getPlanRenderer().setStationMode(mode);
		map.repaint();
	}

	private void setSegmentMode(SegmentMode mode)
	{
		map.getPlanRenderer().setSegmentMode(mode);
		map.repaint();
	}

	private JMenu submenu(String string)
	{
		JMenu menu = new JMenu(string);
		menu.setIcon(new EmptyIcon(24));
		return menu;
	}

	private void addCheckbox(JMenu menu, Action action)
	{
		menu.add(new JCheckBoxMenuItem(action));
	}

	private void setupContent()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		frame.setContentPane(panel);

		map = new ScrollableAdvancedPanel(model.getData(),
				view.getLineNetwork(), PlanRenderer.StationMode.CONVEX,
				PlanRenderer.SegmentMode.CURVE, viewConfig.getStartPosition(),
				10, 15, viewConfig.getBbox());

		statusBar = new StatusBar();

		GridBagConstraintsEditor c = new GridBagConstraintsEditor();
		c.weight(1, 1).fill(GridBagConstraints.BOTH);
		panel.add(map, c.getConstraints());
		c.weight(1, 0).fill(GridBagConstraints.HORIZONTAL);
		c.gridPos(0, 1);
		panel.add(statusBar, c.getConstraints());
	}

	protected void updateStatusBar(int x, int y)
	{
		double lon = map.getMapWindow().getPositionLon(x);
		double lat = map.getMapWindow().getPositionLat(y);

		Node node = mouseNode(x, y);

		String stationName = node == null ? "none" : node.station.getName();

		statusBar.setText(String.format(
				"Location: %d,%d, Coordinates: %.6f,%.6f, Station: %s", x, y,
				lon, lat, stationName));
	}

	protected Node mouseNode(int x, int y)
	{
		Node best = closestNode(x, y);

		double sx = map.getMapWindow().longitudeToX(best.location.lon);
		double sy = map.getMapWindow().latitudeToY(best.location.lat);

		double dx = Math.abs(sx - x);
		double dy = Math.abs(sy - y);
		double d = Math.sqrt(dx * dx + dy * dy);

		if (d < 5) {
			return best;
		}

		return null;
	}

	protected Node closestNode(int x, int y)
	{
		double lon = map.getMapWindow().getPositionLon(x);
		double lat = map.getMapWindow().getPositionLat(y);

		LineNetwork lineNetwork = map.getPlanRenderer().getLineNetwork();

		double bestDistance = Double.MAX_VALUE;
		Node best = null;

		// TODO: use an index to speed this up
		for (Node node : lineNetwork.nodes) {
			Coordinate location = node.location;
			double d = WGS84.haversineDistance(location.getLongitude(),
					location.getLatitude(), lon, lat);
			if (d < bestDistance) {
				bestDistance = d;
				best = node;
			}
		}

		return best;
	}

}

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

package org.openmetromaps.maps.actions;

import org.openmetromaps.maps.MapEditor;

import de.topobyte.swing.util.action.SimpleBooleanAction;

public abstract class MapEditorBooleanAction extends SimpleBooleanAction
{

	private static final long serialVersionUID = 1L;

	protected MapEditor mapEditor;

	public MapEditorBooleanAction(MapEditor mapEditor, String name,
			String description)
	{
		super(name, description);
		this.mapEditor = mapEditor;
	}

}

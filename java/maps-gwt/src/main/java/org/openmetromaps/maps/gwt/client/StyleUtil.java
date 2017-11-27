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

package org.openmetromaps.maps.gwt.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.UIObject;

public class StyleUtil
{

	public static void setSize(UIObject object, double width, double height,
			Unit unit)
	{
		setSize(object.getElement(), width, height, unit);
	}

	public static void setSize(Element element, double width, double height,
			Unit unit)
	{
		setSize(element.getStyle(), width, height, unit);
	}

	public static void setSize(Style style, double width, double height,
			Unit unit)
	{
		style.setWidth(width, unit);
		style.setHeight(height, unit);
	}

	public static void absoluteTopRight(UIObject object, double top,
			double right, Unit unit)
	{
		absoluteTopRight(object.getElement(), top, right, unit);
	}

	public static void absoluteTopRight(Element element, double top,
			double right, Unit unit)
	{
		absoluteTopRight(element.getStyle(), top, right, unit);
	}

	public static void absoluteTopRight(Style style, double top, double right,
			Unit unit)
	{
		style.setPosition(Position.ABSOLUTE);
		style.setTop(3, Unit.EM);
		style.setRight(1, Unit.EM);
	}

	public static void marginTop(UIObject object, double value, Unit unit)
	{
		marginTop(object.getElement(), value, unit);
	}

	public static void marginTop(Element element, double value, Unit unit)
	{
		marginTop(element.getStyle(), value, unit);
	}

	public static void marginTop(Style style, double value, Unit unit)
	{
		style.setMarginTop(value, unit);
	}

	public static void absolute(UIObject object, double top, double right,
			double bottom, double left, Unit unit)
	{
		absolute(object.getElement(), top, right, bottom, left, unit);
	}

	public static void absolute(Element element, double top, double right,
			double bottom, double left, Unit unit)
	{
		absolute(element.getStyle(), top, right, bottom, left, unit);
	}

	public static void absolute(Style style, double top, double right,
			double bottom, double left, Unit unit)
	{
		style.setPosition(Position.ABSOLUTE);
		style.setLeft(0, unit);
		style.setRight(0, unit);
		style.setTop(0, unit);
		style.setBottom(0, unit);
	}

}
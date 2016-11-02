package org.openfact.representations.idm.ubl.common;

import java.util.List;

public class StowageRepresentation {
	protected String id;
	protected String locationID;
	protected List<LocationCommAggRepresentation> location;
	protected List<DimensionRepresentation> measurementDimension;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationID() {
		return locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public List<LocationCommAggRepresentation> getLocation() {
		return location;
	}

	public void setLocation(List<LocationCommAggRepresentation> location) {
		this.location = location;
	}

	public List<DimensionRepresentation> getMeasurementDimension() {
		return measurementDimension;
	}

	public void setMeasurementDimension(List<DimensionRepresentation> measurementDimension) {
		this.measurementDimension = measurementDimension;
	}

}

package ru.unlegit.cdn.coordinator.model;

public record GeoPosition(double lat, double lon) {

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = Math.toRadians(lon2) - Math.toRadians(lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371.0 * c;
    }

    public static GeoPosition parseGeoPosition(String source) {
        String[] parts = source.split(",");
        double lat = Double.parseDouble(parts[0]);
        double lon = Double.parseDouble(parts[1]);

        return new GeoPosition(lat, lon);
    }

    public double distanceTo(GeoPosition position) {
        return haversine(lat, lon, position.lat, position.lon);
    }
}
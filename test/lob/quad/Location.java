package lob.quad;


import lob.quadtree.HasPoint;

/**
 * Helper test class implementing the HasPoint interface
 * 
 * @author Jos&eacute; Paulo Leal {@code zp@dcc.fc.up.pt}
 */

record Location(String name, double latitude, double longitude) implements HasPoint {
    static double RADIUS = 0.0001D; // 11.13 m

    @Override
    public double x() {
        return longitude;
    }

    @Override
    public double y() {
        return latitude;
    }

    @Override
    public String toString() {
        return name+" "+latitude+","+longitude;
    }
}

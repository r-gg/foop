package foop.a1.server.entities;


public record Position(int x, int y) {

    public double euclideanDistance(Position other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Calculates the projection of b onto the line defined by a and c
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static Position calculateProjection(Position a, Position b, Position c) {
        double ax = a.x;
        double ay = a.y;
        double bx = b.x;
        double by = b.y;
        double cx = c.x;
        double cy = c.y;

        double dx = cx - ax;
        double dy = cy - ay;
        double u = ((bx - ax) * dx + (by - ay) * dy) / (dx * dx + dy * dy);

        double px = ax + u * dx;
        double py = ay + u * dy;

        return new Position((int) px, (int) py);
    }

    /**
     * Returns a position that is steps away from this position in the direction opposite of the other position
     * @param steps
     * @return
     */
    public Position moveAwayFrom(Position other, int steps) {
        double ratio = (double) steps / this.euclideanDistance(other);
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        int newX = this.x - (int) (dx * ratio);
        int newY = this.y - (int) (dy * ratio);
        return new Position(newX, newY);
    }

    /**
     * Returns a position that is "steps" away from this position in the direction of the other position
     * @param other
     * @param steps
     * @return
     */
    public Position moveTowards(Position other, int steps){
        double ratio = (double) steps / this.euclideanDistance(other);
        int dx = other.x - this.x;
        int dy = other.y - this.y;
        int newX = this.x + (int) (dx * ratio);
        int newY = this.y + (int) (dy * ratio);
        return new Position(newX, newY);
    }

    public boolean isBetweenWithinPerimeter(Position maybeBetween, Position other, int perimeter) {
        // check if maybeBetween is in the rectangle defined by this and other
        if (maybeBetween.x < Math.min(this.x, other.x) || maybeBetween.x > Math.max(this.x, other.x)) {
            return false;
        }
        if (maybeBetween.y < Math.min(this.y, other.y) || maybeBetween.y > Math.max(this.y, other.y)) {
            return false;
        }
        // if it is the as this or other then false
        if (maybeBetween.equals(this) || maybeBetween.equals(other)) {
            return false;
        }

        // find projection of maybeBetween on the line defined by this and other
        // if the distance between the projection and maybeBetween is less than the perimeter, then maybeBetween is between this and other

        Position projection = calculateProjection(this, maybeBetween, other);
        double distance = projection.euclideanDistance(maybeBetween);
        return distance <= perimeter;
    }

    public Position add(Position other) {
        return new Position(this.x + other.x, this.y + other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }
}
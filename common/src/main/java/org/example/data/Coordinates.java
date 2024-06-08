package org.example.data;

import java.io.Serializable;
import java.util.Objects;


public class Coordinates implements Serializable {

    private static final long serialVersionUID = 2372220107267042787L;
    private Integer x;
    private float y;

    public Coordinates(Integer x, float y) {
        this.y = y;
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates that = (Coordinates) obj;
        return Integer.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "( x: " + this.x + ", y: " + this.y + ")";
    }
}

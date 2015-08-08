/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package item;

/**
 *
 * @author fahad_000
 */
public class BorderItem {

    long countAt;
    int minX, maxX, minY, maxY, width, height;

    public BorderItem() {
    }

    public BorderItem(long countAt, int minX, int maxX, int minY, int maxY, int width, int height) {
        this.countAt = countAt;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getCountAt() {
        return countAt;
    }

    public void setCountAt(long countAt) {
        this.countAt = countAt;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    @Override
    public String toString() {
        return "BorderItem{" + "countAt=" + countAt + ", minX=" + minX + ", maxX=" + maxX + ", minY=" + minY + ", maxY=" + maxY + '}';
    }

}

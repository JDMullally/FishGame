package model;


import org.junit.Test;

import static org.junit.Assert.*;

public class CanvasTest {
    private int x;
    private int y;
    private int width;
    private int height;
    private Canvas emptyCanvas;
    private Canvas testCanvas;

    private void init() {
        this.x = 5;
        this.y = 6;
        this.height = 500;
        this.width = 600;
        this.emptyCanvas = new Canvas();
        this.testCanvas =  new Canvas(x,y,width,height);
    }

    @Test
    public void getX1() {
        init();

        assertEquals(0,this.emptyCanvas.getX());
    }

    @Test
    public void getX2() {
        init();

        assertEquals(this.x, this.testCanvas.getX());
    }

    @Test
    public void getY1() {
        init();

        assertEquals(0,this.emptyCanvas.getY());
    }
    @Test
    public void getY2() {
        init();

        assertEquals(this.y,this.testCanvas.getY());
    }

    @Test
    public void getWidth1() {
        init();

        assertEquals(0,this.emptyCanvas.getWidth());
    }

    @Test
    public void getWidth2() {
        init();

        assertEquals(this.width,this.testCanvas.getWidth());
    }

    @Test
    public void getHeight1() {
        init();

        assertEquals(0,this.emptyCanvas.getHeight());
    }

    @Test
    public void getHeight2() {
        init();

        assertEquals(this.height,this.testCanvas.getHeight());
    }
}

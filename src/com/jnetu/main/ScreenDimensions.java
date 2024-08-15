package com.jnetu.main;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenDimensions {
    public final int screenWidth;
    private final int screenHeight;
    private int newWidth;
    private int newHeight;
    private final int xOffset;
    private final int yOffset;

    public ScreenDimensions(int gameWidth, int gameHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.width;
        this.screenHeight = screenSize.height;

        this.newWidth = screenWidth;
        this.newHeight = (screenWidth * gameHeight) / gameWidth;

        if (newHeight > screenHeight) {
            this.newHeight = screenHeight;
            this.newWidth = (screenHeight * gameWidth) / gameHeight;
        }

        this.xOffset = (screenWidth - newWidth) / 2;
        this.yOffset = (screenHeight - newHeight) / 2;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getNewWidth() {
        return newWidth;
    }

    public int getNewHeight() {
        return newHeight;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int scaleX(int originalX, int gameWidth) {
        return (originalX - xOffset) * gameWidth / newWidth;
    }

    public int scaleY(int originalY, int gameHeight) {
        return (originalY - yOffset) * gameHeight / newHeight;
    }
}

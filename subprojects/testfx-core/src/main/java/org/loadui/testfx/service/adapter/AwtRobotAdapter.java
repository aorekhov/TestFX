/*
 * Copyright 2013-2014 SmartBear Software
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the Licence for the specific language governing permissions
 * and limitations under the Licence.
 */
package org.loadui.testfx.service.adapter;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import com.google.common.collect.ImmutableMap;

public class AwtRobotAdapter {

    //---------------------------------------------------------------------------------------------
    // CONSTANTS.
    //---------------------------------------------------------------------------------------------

    private static final Map<MouseButton, Integer> AWT_BUTTONS = ImmutableMap.of(
        MouseButton.PRIMARY, InputEvent.BUTTON1_MASK,
        MouseButton.MIDDLE, InputEvent.BUTTON2_MASK,
        MouseButton.SECONDARY, InputEvent.BUTTON3_MASK
    );

    //---------------------------------------------------------------------------------------------
    // PRIVATE FIELDS.
    //---------------------------------------------------------------------------------------------

    private Robot awtRobot;

    //---------------------------------------------------------------------------------------------
    // METHODS.
    //---------------------------------------------------------------------------------------------

    // ROBOT.

    public void robotCreate() {
        if (isAwtEnvironmentHeadless()) {
            throw new RuntimeException("environment is headless");
        }
        initializeAwtToolkit();
        awtRobot = createAwtRobot();
    }

    public void robotDestroy() {
        throw new UnsupportedOperationException();
    }

    public Robot getRobotInstance() {
        return awtRobot;
    }

    // KEY.

    public void keyPress(KeyCode key) {
        awtRobot.keyPress(convertToAwtKey(key));
    }

    public void keyRelease(KeyCode key) {
        awtRobot.keyRelease(convertToAwtKey(key));
    }

    // MOUSE.

    public Point2D getMouseLocation() {
        return convertFromAwtPoint(MouseInfo.getPointerInfo().getLocation());
    }

    public void mouseMove(Point2D location) {
        awtRobot.mouseMove((int) location.getX(), (int) location.getY());
    }

    public void mousePress(MouseButton button) {
        awtRobot.mousePress(convertToAwtButton(button));
    }

    public void mouseRelease(MouseButton button) {
        awtRobot.mouseRelease(convertToAwtButton(button));
    }

    public void mouseWheel(int wheelAmount) {
        awtRobot.mouseWheel(wheelAmount);
    }

    // CAPTURE.

    public Color getCapturePixelColor(Point2D location) {
        throw new UnsupportedOperationException();
    }

    public Image getCaptureRegion(Rectangle2D region) {
        Rectangle awtRectangle = convertToAwtRectangle(region);
        BufferedImage awtBufferedImage = awtRobot.createScreenCapture(awtRectangle);
        return convertFromAwtBufferedImage(awtBufferedImage);
    }

    // TIMER.

    /**
     * Block until events in the queue are processed.
     */
    public void timerWaitForIdle() {
        throw new UnsupportedOperationException();
    }

    //---------------------------------------------------------------------------------------------
    // PRIVATE METHODS.
    //---------------------------------------------------------------------------------------------

    private Robot createAwtRobot() {
        try {
            return new Robot();
        }
        catch (AWTException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void initializeAwtToolkit() {
        Toolkit.getDefaultToolkit();
    }

    private boolean isAwtEnvironmentHeadless() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
    }

    private Point2D convertFromAwtPoint(Point awtPoint) {
        return new Point2D(awtPoint.getX(), awtPoint.getY());
    }

    private Image convertFromAwtBufferedImage(BufferedImage awtBufferedImage) {
        return SwingFXUtils.toFXImage(awtBufferedImage, null);
    }

    private int convertToAwtButton(MouseButton button) {
        return AWT_BUTTONS.get(button);
    }

    private Rectangle convertToAwtRectangle(Rectangle2D rectangle) {
        return new Rectangle(
            (int) rectangle.getMinX(), (int) rectangle.getMinY(),
            (int) rectangle.getWidth(), (int) rectangle.getHeight()
        );
    }

    @SuppressWarnings("deprecation")
    private int convertToAwtKey(KeyCode keyCode) {
        return keyCode.impl_getCode();
    }

}
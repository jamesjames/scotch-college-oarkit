package com.scotch.OARKit.java.helpers;

import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/*
 *
 * JInput Joystick
 *
 *
 * @author TheUzo007
 *         http://theuzo007.wordpress.com
 *
 *
 * To use this you need JInput libraries and its files. http://java.net/projects/jinput
 *
 * This class is intended for use with joysticks of stick or gamepad type (JInput type),
 * like Logitech Dual Action which is a stick type or Xbox MadCatz which is a gamepad type.
 * It can be used with other types to, but each controller has different components, therefore,
 * some methods that I wrote are not useful with other types of controller. But
 * you can always use getComponentValue method and specify controller component
 * identifier that you need or add your own methods.
 *
 * JInput javadoc: http://www.newdawnsoftware.com/resources/jinput/apidocs
 *
 *
 * More on the blog: http://theuzo007.wordpress.com/2012/09/02/joystick-in-java-with-jinput
 *
 */

public class JInputJoystick {

    private Controller controller;
    public Component[] components;

    // Controller buttons states
    private static ArrayList<Boolean> buttonsValues;




    /*
     * Creates a controller, of type that has been given.
     *
     * @param controllerType Desired controller type.
     */
    public JInputJoystick(Controller.Type controllerType)
    {
        initialize();
        initController(controllerType, null);
    }

    /*
     * Creates a controller, of one of the types that has been given.
     * Controller type which is first found will be created.
     *
     * @param controllerType_1 Desired controller type.
     * @param controllerType_2 Desired controller type.
     */
    public JInputJoystick(Controller.Type controllerType_1, Controller.Type controllerType_2)
    {
        initialize();
        initController(controllerType_1, controllerType_2);
    }

    private void initialize()
    {
        this.controller = null;
        this.buttonsValues = new ArrayList<Boolean>();
    }

    /*
     * Save first founded controller of given type.
     *
     * @param controllerType Desired controller type.
     */
    private void initController(Controller.Type controllerType_1, Controller.Type controllerType_2)
    {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for(int i=0; i < controllers.length && controller == null; i++) {
            if(
                    controllers[i].getType() == controllerType_1 ||
                            controllers[i].getType() == controllerType_2
                    )
            {
                controller = controllers[i];
                break;
            }
        }
    }







    /*
     * Checks if the controller is connected/valid.
     * It also poll the controller for data, but it doesn't save states
     * of the buttons into buttons array list that is used by getButtonsValues()
     * and getButtonValue(int index) methods.
     *
     * @see joystick.JInputJoystick#pollController()
     *
     * @return True if controller is connected, false otherwise.
     */
    public boolean isControllerConnected()
    {
        try {
            return controller.poll();
        } catch (Exception e) {
            return false;
        }
    }


    /*
     * Gets the controller type.
     * Throws exception if controller doesn't exists.
     *
     * @return Type of the controller.
     */
    public Controller.Type getControllerType()
    {
        return controller.getType();
    }


    /*
     * Gets the human readable controller name.
     * Throws exception if controller doesn't exists.
     *
     * @return Controller name.
     */
    public String getControllerName()
    {
        return controller.getName();
    }


    /*
     * Check and save current controller state (controller components values).
     * Must be called every time before using controller state methods (eg. method for x axis value),
     * so that you get latest controller components values.
     *
     * @return True if controller is connected/valid, false otherwise.
     */
    public boolean pollController()
    {
        boolean isControllerValid;

        // Clear previous values of buttons.
        buttonsValues.clear();

        try {
            isControllerValid = controller.poll();
        } catch (Exception e) {
            return false;
        }
        if(!isControllerValid)
            return false;

        components = controller.getComponents();

        for(int i=0; i < components.length; i++) {
            Component component = components[i];
            //System.out.println(component);
            // Add states of the buttons
            if(!component.isAnalog())
                if(component.getPollData() == 1.0f)
                    buttonsValues.add(Boolean.TRUE);
                else
                    buttonsValues.add(Boolean.FALSE);
        }

        return isControllerValid;
    }


    /*
     * Checks if component with given identifier exists.
     *
     * @param identifier Identifier that correspond to component.
     * @return True if component exists or false if not exists.
     */
    public boolean componentExists(Identifier identifier)
    {
        Component component = controller.getComponent(identifier);

        if(component != null)
            return true;
        else
            return false;
    }


    /*
     * Gets value of component with given identifier.
     *
     * @param identifier Identifier that correspond to component from which we need value.
     * @return Component value.
     */
    public float getComponentValue(Identifier identifier){
        return controller.getComponent(identifier).getPollData();
    }


    /*
     * How many buttons does controller have?
     *
     * @return Number of buttons on a controller.
     */
    public int getNumberOfButtons()
    {
        return buttonsValues.size();
    }

    /*
     * Controller buttons states. Index of element in array list correspond to
     * button number on the controller.
     * If element is true then button is pressed, if element is false then
     * button is not pressed.
     *
     * @return Array list of states of all controller buttons.
     */
    public ArrayList<Boolean> getButtonsValues()
    {
        return buttonsValues;
    }

    /*
     * Gets value of required button.
     *
     * @param index Index of a button in array list.
     * @return True if button is pressed, false otherwise.
     */
    public static boolean getButtonValue(int index)
    {
        return buttonsValues.get(index);
    }


    /*
     * Value of axis named X Axis.
     *
     * @return X Axis value.
     */
    public float getXAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.X;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named X Axis in percentage.
     * Percentages increases from left to right.
     * If idle (in center) returns 50, if joystick axis is pushed to the left
     * edge returns 0 and if it's pushed to the right returns 100.
     *
     * @return X Axis value in percentage.
     */
    public int getXAxisPercentage()
    {
        float xAxisValue = this.getXAxisValue();
        int xAxisValuePercentage = (int)((2 - (1 - xAxisValue)) * 100) / 2;

        return xAxisValuePercentage;
    }


    /*
     * Value of axis named Y Axis.
     *
     * @return Y Axis value.
     */
    public float getYAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.Y;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named Y Axis in percentage.
     * Percentages increases from top to bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the top
     * edge returns 0 and if it is pushed to the bottom returns 100.
     *
     * @return Y Axis value in percentage.
     */
    public int getYAxisPercentage()
    {
        float yAxisValue = this.getYAxisValue();
        int yAxisValuePercentage = (int)((2 - (1 - yAxisValue)) * 100) / 2;

        return yAxisValuePercentage;
    }


    /*
     * Value of axis named Z Rotation.
     *
     * @return Z Rotation value.
     */
    public float getZRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RZ;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named Z Rotation in percentage.
     * Percentages increases from top to bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the top
     * edge returns 0 and if it is pushed to the bottom returns 100.
     *
     * @return Z Rotation value in percentage.
     */
    public int getZRotationPercentage()
    {
        float zRotation = this.getZRotationValue();
        int zRotationValuePercentage = (int)((2 - (1 - zRotation)) * 100) / 2;

        return zRotationValuePercentage;
    }


    /*
     * Value of axis named Z Axis.
     *
     * @return Z Axis value.
     */
    public float getZAxisValue()
    {
        Identifier identifier = Component.Identifier.Axis.Z;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named Z Axis in percentage.
     * Percentages increases from left to right.
     * If idle (in center) returns 50, if joystick axis is pushed to the left
     * edge returns 0 and if it's pushed to the right returns 100.
     *
     * @return Z Axis value in percentage.
     */
    public int getZAxisPercentage()
    {
        float zAxisValue = this.getZAxisValue();
        int zAxisValuePercentage = (int)((2 - (1 - zAxisValue)) * 100) / 2;

        return zAxisValuePercentage;
    }


    /*
     * Value of axis named X Rotation.
     *
     * @return X Rotation value.
     */
    public float getXRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RX;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named X Rotation in percentage.
     * Percentages increases from left to right.
     * If idle (in center) returns 50, if joystick axis is pushed to the left
     * edge returns 0 and if it's pushed to the right returns 100.
     *
     * @return X Rotation value in percentage.
     */
    public int getXRotationPercentage()
    {
        float xRotationValue = this.getXRotationValue();
        int xRotationValuePercentage = (int)((2 - (1 - xRotationValue)) * 100) / 2;

        return xRotationValuePercentage;
    }


    /*
     * Value of axis named Y Rotation.
     *
     * @return Y Rotation value.
     */
    public float getYRotationValue()
    {
        Identifier identifier = Component.Identifier.Axis.RY;
        return controller.getComponent(identifier).getPollData();
    }

    /*
     * Value of axis named Y Rotation in percentage.
     * Percentages increases from top to bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the top
     * edge returns 0 and if it is pushed to the bottom returns 100.
     *
     * @return Y Rotation value in percentage.
     */
    public int getYRotationPercentage()
    {
        float yRotationValue = this.getYRotationValue();
        int yRotationValuePercentage = (int)((2 - (1 - yRotationValue)) * 100) / 2;

        return yRotationValuePercentage;
    }


    /*
     * Gets position of the Hat Switch.
     * Float number that is returned by this method correspond with
     * positions in the JInput class Component.POV.
     *
     * @return Float number that corresponds with the Hat Switch position.
     */
    public float getHatSwitchPosition()
    {
        Identifier identifier = Component.Identifier.Axis.POV;
        //Logger.info(controller.getComponent(identifier).getPollData());
        return controller.getComponent(identifier).getPollData();
    }






    /* Left joystick */

    /*
     * X position of left controller joystick.
     *
     * The same as method getXAxisValue().
     *
     * @see joystick.JInputJoystick#getXAxisValue()
     *
     * @return Float value (from -1.0f to 1.0f) corresponding to left controller joystick on x coordinate.
     */
    public float getX_LeftJoystick_Value()
    {
        return this.getXAxisValue();
    }

    /*
     * X position, in percentages, of left controller joystick.
     *
     * The same as method getXAxisPercentage().
     *
     * @see joystick.JInputJoystick#getXAxisPercentage()
     *
     * @return Int value (from 0 to 100) corresponding to left controller joystick on x coordinate.
     */
    public int getX_LeftJoystick_Percentage()
    {
        return this.getXAxisPercentage();
    }


    /*
     * Y position of left controller joystick.
     *
     * The same as method getYAxisValue().
     *
     * @see joystick.JInputJoystick#getYAxisValue()
     *
     * @return Float value (from -1.0f to 1.0f) corresponding to left controller joystick on y coordinate.
     */
    public float getY_LeftJoystick_Value()
    {
        return this.getYAxisValue();
    }

    /*
     * Y position, in percentages, of left controller joystick.
     *
     * The same as method getYAxisPercentage().
     *
     * @see joystick.JInputJoystick#getYAxisPercentage()
     *
     * @return Int value (from 0 to 100) corresponding to left controller joystick on y coordinate.
     */
    public int getY_LeftJoystick_Percentage()
    {
        return this.getYAxisPercentage();
    }


    /* Right joystick */

    /*
     * X position of right controller joystick.
     *
     * The same as method getZAxisValue() if controller type is Controller.Type.STICK.
     * The same as method getXRotationValue() if controller type is Controller.Type.GAMEPAD.
     *
     * @see joystick.JInputJoystick#getZAxisValue()
     * @see joystick.JInputJoystick#getXRotationValue()
     *
     * @return Float value (from -1.0f to 1.0f) corresponding to right controller joystick on x coordinate.
     */
    public float getX_RightJoystick_Value()
    {
        float xValueRightJoystick;

        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            xValueRightJoystick = this.getZAxisValue();
        }
        // gamepad type controller
        else
        {
            xValueRightJoystick = this.getXRotationValue();
        }

        return xValueRightJoystick;
    }

    /*
     * X position, in percentages, of right controller joystick.
     *
     * The same as method getZAxisPercentage() if controller type is Controller.Type.STICK.
     * The same as method getXRotationPercentage() if controller type is Controller.Type.GAMEPAD.
     *
     * @see joystick.JInputJoystick#getZAxisPercentage()
     * @see joystick.JInputJoystick#getXRotationPercentage()
     *
     * @return Int value (from 0 to 100) corresponding to right controller joystick on x coordinate.
     */
    public int getX_RightJoystick_Percentage()
    {
        int xValueRightJoystickPercentage;

        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            xValueRightJoystickPercentage = this.getZAxisPercentage();
        }
        // gamepad type controller
        else
        {
            xValueRightJoystickPercentage = this.getXRotationPercentage();
        }

        return xValueRightJoystickPercentage;
    }


    /*
     * Y position of right controller joystick.
     *
     * The same as method getZRotationValue() if controller type is Controller.Type.STICK.
     * The same as method getYRotationValue() if controller type is Controller.Type.GAMEPAD.
     *
     * @see joystick.JInputJoystick#getZRotationValue()
     * @see joystick.JInputJoystick#getYRotationValue()
     *
     * @return Float value (from -1.0f to 1.0f) corresponding to right controller joystick on y coordinate.
     */
    public float getY_RightJoystick_Value()
    {
        float yValueRightJoystick;

        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            yValueRightJoystick = this.getZRotationValue();
        }
        // gamepad type controller
        else
        {
            yValueRightJoystick = this.getYRotationValue();
        }

        return yValueRightJoystick;
    }

    /*
     * Y position, in percentages, of right controller joystick.
     *
     * The same as method getZRotationPercentage() if controller type is Controller.Type.STICK.
     * The same as method getYRotationPercentage() if controller type is Controller.Type.GAMEPAD.
     *
     * @see joystick.JInputJoystick#getZRotationPercentage()
     * @see joystick.JInputJoystick#getYRotationPercentage()
     *
     * @return Int value (from 0 to 100) corresponding to right controller joystick on y coordinate.
     */
    public int getY_RightJoystick_Percentage()
    {
        int yValueRightJoystickPercentage;

        // stick type controller
        if(this.controller.getType() == Controller.Type.STICK)
        {
            yValueRightJoystickPercentage = this.getZRotationPercentage();
        }
        // gamepad type controller
        else
        {
            yValueRightJoystickPercentage = this.getYRotationPercentage();
        }

        return yValueRightJoystickPercentage;
    }
}

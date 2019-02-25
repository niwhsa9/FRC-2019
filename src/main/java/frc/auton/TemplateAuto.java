package frc.auton;

import frc.subsystem.*;
import frc.utility.Threaded;

public class TemplateAuto extends Threaded { 
    Drive drive = Drive.getInstance();
    Elevator elevator = Elevator.getInstance();
    Arm arm = Arm.getInstance();
    BallIntake ballIntake = BallIntake.getInstance();
    Climber climber = Climber.getInstance(); 
    CollisionManager collisionManager = CollisionManager.getInstance();
    RobotTracker robotTracker = RobotTracker.getInstance();
    HatchIntake hatchIntake = HatchIntake.getInstance();
    Manipulator manipulator = Manipulator.getInstance();
    Turret turret = Turret.getInstance();

    public TemplateAuto() {
        
    }

    @Override
    public void update() {

    }

}
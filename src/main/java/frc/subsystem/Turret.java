// Copyright 2019 FRC Team 3476 Code Orange

package frc.subsystem;

import frc.robot.Constants;
import frc.utility.JetsonUDP;
import frc.utility.LazyTalonSRX;
import frc.utility.Threaded;
import frc.utility.VisionTarget;

import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Turret extends Threaded {
	
	//private static Turret instance = new Turret();
	private LazyTalonSRX turretTalon;
	private static DigitalInput turretHallEffect;
	private double angle =0;
	private double prevSign = 1;
	JetsonUDP visionData = JetsonUDP.getInstance();


	private static final Turret instance = new Turret();

	public static Turret getInstance() {
		return instance;
	}

	private Turret() {
		turretTalon = new LazyTalonSRX(Constants.TurretMotorId);
		turretTalon.setSensorPhase(false);
		turretTalon.setInverted(false);
		turretTalon.config_kP(0, Constants.kTurretP, Constants.TimeoutMs);
		turretTalon.config_kI(0, Constants.kTurretI, Constants.TimeoutMs);
		turretTalon.config_kD(0, Constants.kTurretD, Constants.TimeoutMs);
		turretHallEffect = new DigitalInput(Constants.TurretLimitId);
	}
	
	public void setAngle(double angle) {
		turretTalon.set(ControlMode.Position, angle * Constants.EncoderTicksPerDegree);
	}
	
	public void setSpeed(double speed) {
		turretTalon.set(ControlMode.Velocity, speed * Constants.EncoderTicksPerDegree);
	}
	
	public double getSpeed() {
		return turretTalon.getSelectedSensorVelocity(Constants.TurretMotorId) * Constants.DegreesPerEncoderTick;
	}
	
	public double getAngle() {
		return turretTalon.getSelectedSensorPosition(Constants.TurretMotorId) * Constants.DegreesPerEncoderTick;
	}
	
	public double getTargetAngle() {
		return turretTalon.getSetpoint() * Constants.DegreesPerEncoderTick;
	}
	
	//Return current
	public double getOutputCurrent() {
		return turretTalon.getOutputCurrent();
	}

	public void homeTurret() {
		turretTalon.setSelectedSensorPosition(0, 0, 10); // Zero encoder
		double dir = 1; // left
		boolean isTriggered = false;

		while (!isTriggered) {
		  turretTalon.set(ControlMode.PercentOutput, Constants.TurretHomingPower * dir);
			
			if (getAngle() >= Constants.TurretMaxHomingAngle) dir *= -1;//Switch direction

			if (turretHallEffect.get()) {
				turretTalon.set(ControlMode.PercentOutput, 0);
				turretTalon.setSelectedSensorPosition(0, 0, 10); // Zero encoder
			}
		}
	}
	
	@Override
	public void update() {
		VisionTarget[] target = visionData.getTargets();
		//printf(target[0].x);
		//System.out.println(turretTalon.getSelectedSensorPosition());
		if(target.length > 0 && target[0] != null) {
			System.out.println("target x " + target[0].x + " angle " + getAngle() + " desired " + angle);
			double error = target[0].x/640.0 - 0.5;
			angle = getAngle() + error * 30.0;	
			prevSign = Math.abs(error)/error;
			setAngle(angle);
		} else {
			//angle += 0.1*prevSign;
			//setAngle(angle);
		}
		//System.out.println(angle);
	//	turretTalon.set(ControlMode.PercentOutput, 0.3);
	}
}

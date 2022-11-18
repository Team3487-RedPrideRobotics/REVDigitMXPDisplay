package frc.robot.util;

import frc.robot.Constants;

public class TrajectoryHelper {

    /**
     * takes in desired entry angle and the distance and height from target, and returns the required angle and velocity a projectile must start at to reach the target
     * 
     * @param delta_height height from the projectile's initial position to the target in meters
     * @param distance horizontal distance from the target in meters
     * @param entry_angle desired angle for the projectile to enter the target at in radians
     * @return double[] {target_angle, target_velocity} where target_angle and target_velocity are the angle in radians and velocity in meters per second, respectively, that a projectile must start at to reach the target.
     *
     */
    
    public static double[] calculateTargetTrajectory(double delta_height, double distance, double entry_angle){
        double target_angle = Math.atan(2*(delta_height/distance)-Math.tan(entry_angle));
        return(new double[] {target_angle, 1/Math.cos(target_angle) * Math.sqrt(Constants.ACCELERATION_OF_GRAVITY*distance/(Math.abs(Math.tan(entry_angle)-Math.tan(target_angle))))});
    }
}

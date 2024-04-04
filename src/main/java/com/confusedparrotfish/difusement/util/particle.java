package com.confusedparrotfish.difusement.util;

import java.util.ArrayList;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class particle {
    /**
     * draw a line of particles from point a to b
     */
    public static void line(ServerLevel world, Vec3 a, Vec3 b, float density) {
        double d = a.distanceTo(b);
        double r = d/density;
        double l = (d-Math.floor(r)*density)/r+r;
        Vec3 s = new Vec3(-(a.x()-b.x())/l, -(a.y()-b.y())/l, -(a.z()-b.z())/l);

        spawnparticle(world, a, ParticleTypes.FLAME);

        for (int i = 0; i < Math.floor(r); i++) {
            Vec3 t = new Vec3(s.x()*(i+1)+a.x(), s.y()*(i+1)+a.y(), s.z()*(i+1)+a.z());

            spawnparticle(world, t, ParticleTypes.FLAME);
        }
    }

    /**
     * calculate point in a line from a to b
     */
    public static ArrayList<Vec3> linepoints(ServerLevel world, Vec3 a, Vec3 b, float density) {
        ArrayList<Vec3> rtn = new ArrayList<Vec3>();
        double d = a.distanceTo(b);
        double r = d/density;
        Vec3 s = new Vec3(-(a.x()-b.x())/r, -(a.y()-b.y())/r, -(a.z()-b.z())/r);

        // System.out.println(srvec(s)+", count: "+Math.floor(r));

        for (int i = 0; i < Math.floor(r); i++) {
            rtn.add(new Vec3(s.x()*(i+1)+a.x(), s.y()*(i+1)+a.y(), s.z()*(i+1)+a.z()));
        }
        return rtn;
    }
    
    // private static String srvec(Vec3 s) {
    //     return "x: "+s.x()+", y: "+s.y()+", z: "+s.z();
    // }

    public static void spawnparticle(ServerLevel world, Vec3 vec3, SimpleParticleType particle) {
        world.sendParticles(particle, vec3.x(), vec3.y(), vec3.z(), 15, 0, 0, 0, 0);
    }

    public static ArrayList<Vec3> mixvec3list(ArrayList<Vec3> a,ArrayList<Vec3>b) {
        for (int i = 0; i < b.size(); i++) {
            a.add(b.get(i));
        }
        return a;
    }

    /**
     * a circle centered on the y (multi rot version comming soon)
     * @param world
     * @param add
     * @param d
     * @param u
     */
    public static void circle(ServerLevel world, Vec3 pos, double r, float u) {
        double c = (Math.PI*2)*r;
        double rad = Math.floor(c/u);
            
        for (double i = 0; i < Math.PI*2; i+=(Math.PI*2)/rad) {
            spawnparticle(world, pos.add(Math.sin(i)*r,0,Math.cos(i)*r), ParticleTypes.FLAME);
        }
    }
}

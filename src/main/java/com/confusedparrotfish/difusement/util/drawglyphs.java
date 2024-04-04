package com.confusedparrotfish.difusement.util;

import java.util.ArrayList;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class drawglyphs {
    public static void hexagon(ServerLevel world, Vec3 vec, float s, float d) {
        Vec3 p0 = vec.add(0.5, 0, 0.5+s);//0 1
        Vec3 p1 = vec.add(0.5+(0.866*s), 0, 0.5+(0.5*s));//0.866 0.5
        Vec3 p2 = vec.add(0.5+(0.866*s), 0, 0.5-(0.5*s));//0.866 -0.5
        Vec3 p3 = vec.add(0.5, 0, 0.5-s);//0 -1
        Vec3 p4 = vec.add(0.5-(0.866*s), 0, 0.5-(0.5*s));//-0.866 -0.5
        Vec3 p5 = vec.add(0.5-(0.866*s), 0, 0.5+(0.5*s));//-0.866 0.5

        particle.line(world, p0, p1, d);
        particle.line(world, p1, p2, d);
        particle.line(world, p2, p3, d);
        particle.line(world, p3, p4, d);
        particle.line(world, p4, p5, d);
        particle.line(world, p5, p0, d);
    }
    
    public static void fire(ServerLevel world, Vec3 pos, float s, float u) {//fire
        particle.line(world, pos.add(0.5, 0, 0.5),pos.add(0.5-(0.346*s), 0, 0.5+(0.4*s)),u);
        particle.line(world, pos.add(0.5, 0, 0.5),pos.add(0.5+(0.346*s), 0, 0.5+(0.4*s)),u);
        particle.line(world,pos.add(0.5-(0.346*s), 0, 0.5+(0.4*s)),pos.add(0.5+(0.346*s), 0, 0.5+(0.4*s)),u);
        particle.line(world,pos.add(0.5, 0, 0.5),pos.add(0.5+(0.866*s), 0, 0.5-(s/2)),u);
        particle.line(world,pos.add(0.5, 0, 0.5),pos.add(0.5-(0.866*s), 0, 0.5-(s/2)),u);
    }

    public static void water(ServerLevel world, Vec3 pos, float s, float u) {//water
        particle.line(world, pos.add(0.5,0,0.5),pos.add(0.5-(0.346*s),0,0.5+(0.4*s)),u);
        particle.line(world, pos.add(0.5,0,0.5),pos.add(0.5+(0.346*s),0,0.5+(0.4*s)),u);
        particle.line(world, pos.add(0.5,0,0.5+s),pos.add(0.5+(0.866*s),0,0.5-(0.5*s)),u);
        particle.line(world, pos.add(0.5,0,0.5+s),pos.add(0.5-(0.866*s),0,0.5-(0.5*s)),u);
    }
    
    public static void earth(ServerLevel world, Vec3 pos, float s, float u) {//earth
        particle.circle(world,pos.add(0.5,0,0.5),0.5*s,u);
    }

    public static void air(ServerLevel world, Vec3 pos, float s, float u) {
        particle.line(world, pos.add(0.5+(0.433*s),0,0.5+(0.25*s)), pos.add(0.5,0,0.5-s), u);
        particle.line(world, pos.add(0.5-(0.433*s),0,0.5+(0.25*s)), pos.add(0.5,0,0.5-s), u);
        particle.line(world, pos.add(0.5-(0.433*s),0,0.5+(0.25*s)), pos.add(0.5,0,0.5+(0.833*s)), u);
        particle.line(world, pos.add(0.5+(0.433*s),0,0.5+(0.25*s)), pos.add(0.5,0,0.5+(0.833*s)), u);
    }

    public static void power(ServerLevel world, Vec3 pos, float s, float u) {
        particle.line(world,pos.add(0.5,0,0.5+(0.4*s)),pos.add(0.5+(0.346*s),0,0.5+(0.2*s)),u);
        particle.line(world,pos.add(0.5+(0.346*s),0,0.5+(0.2*s)),pos.add(0.5+(0.346*s),0,0.5-(0.2*s)),u);
        particle.line(world,pos.add(0.5+(0.346*s),0,0.5-(0.2*s)),pos.add(0.5,0,0.5-(0.4*s)),u);
        particle.line(world,pos.add(0.5,0,0.5-(0.4*s)),pos.add(0.5-(0.346*s),0,0.5-(0.2*s)),u);
        particle.line(world,pos.add(0.5-(0.346*s),0,0.5-(0.2*s)),pos.add(0.5-(0.346*s),0,0.5+(0.2*s)),u);
        particle.line(world,pos.add(0.5-(0.346*s),0,0.5+(0.2*s)),pos.add(0.5,0,0.5+(0.4*s)),u);
        //to
        particle.line(world,pos.add(0.5,0,0.5+s),pos.add(0.5,0,0.5+(0.4*s)),u);
        particle.line(world,pos.add(0.5-(0.866*s),0,0.5-(0.5*s)),pos.add(0.5-(0.346*s),0,0.5-(0.2*s)),u);
        particle.line(world,pos.add(0.5+(0.866*s),0,0.5-(0.5*s)),pos.add(0.5+(0.346*s),0,0.5-(0.2*s)),u);
    }
    
    //   if (light) {//light
    //     pline(0,0,0.866*s,0.5*s,u)
    //     pline(0,0,-0.866*s,0.5*s,u)
    //     pcirq(0,-(1/3)*s,1/3*s,u)
    //     // pcirq(0,-(1/3)*s,0.025*s,u)
    //     text("light",5,72)
    //   }

    //points
    public static ArrayList<Vec3> hexagonpoints(ServerLevel world, Vec3 vec, float s, float d) {
        Vec3 p0 = vec.add(0.5, 0, 0.5+s);//0 1
        Vec3 p1 = vec.add(0.5+(0.866*s), 0, 0.5+(0.5*s));//0.866 0.5
        Vec3 p2 = vec.add(0.5+(0.866*s), 0, 0.5-(0.5*s));//0.866 -0.5
        Vec3 p3 = vec.add(0.5, 0, 0.5-s);//0 -1
        Vec3 p4 = vec.add(0.5-(0.866*s), 0, 0.5-(0.5*s));//-0.866 -0.5
        Vec3 p5 = vec.add(0.5-(0.866*s), 0, 0.5+(0.5*s));//-0.866 0.5

        return particle.mixvec3list(
            particle.mixvec3list(
            particle.mixvec3list(
            particle.mixvec3list(
            particle.mixvec3list(
            particle.mixvec3list(new ArrayList<Vec3>(),
            particle.linepoints(world, p0, p1, d)),
            particle.linepoints(world, p1, p2, d)),
            particle.linepoints(world, p2, p3, d)),
            particle.linepoints(world, p3, p4, d)),
            particle.linepoints(world, p4, p5, d)),
            particle.linepoints(world, p5, p0, d));
    }
}

package blocksworld.csp;

import blocksworld.world.BlockWorldWithConstraints;

public class DemoConstraint {
    public static void main(String[] args){

        int b = 6;
        int s = 8;

        BlockWorldWithConstraints w = new BlockWorldWithConstraints(b, s);

        DemoCSP d = new DemoCSP(w,"constraint normal");
        d.demo();
    }
}

package com.github.stefanofornari.braincells;

import static org.assertj.core.api.BDDAssertions.then;


/**
 *
 * @author ste
 */
public class Utils {
    
    public static void thenBrainIs(Brain brain, boolean... cells) {
        for(int i=0; i<cells.length; ++i) {
            System.out.print(i + " ");
            then(brain.cells[i].active).isEqualTo(cells[i]);
        }
        System.out.println("");
    }
}


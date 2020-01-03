package com.github.stefanofornari.braincells;

/**
 *
 */
public class BrainTrainer {
    
    private final Brain brain;
    
    public BrainTrainer(Brain brain) {
        this.brain = brain;
    }
    
    public void learn(int[] ons, int[] offs) {
        for (int cell: ons) {
            if (brain.cells[cell].active == false) {
                for (int i = 0; i < brain.cells.length; ++i) {
                    BrainCell c = brain.cells[i];
                    if ((i != cell) && (c.active) && (c.promoters.size() < 3)) {
                        brain.promoter(i, cell);
                    }
                }
            }
        }
        for (int cell: offs) {
            if (brain.cells[cell].active == true) {
                for (int i = 0; i < brain.cells.length; ++i) {
                    BrainCell c = brain.cells[i];
                    if ((i != cell) && (c.active) && (c.detractors.size() < 3)) {
                        brain.detractor(i, cell);
                    }
                }
            }
        }
    }
}

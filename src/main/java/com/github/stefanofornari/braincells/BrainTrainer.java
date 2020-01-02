package com.github.stefanofornari.braincells;

/**
 *
 */
public class BrainTrainer {
    
    private final Brain brain;
    
    public BrainTrainer(Brain brain) {
        this.brain = brain;
    }
    
    
    public void learn(int activeCell) {
        if (brain.cells[activeCell].active == false) {
            int nConnections = brain.resistance;
            for (int i = 0; (i<brain.cells.length) && (nConnections > 0); ++i) {
                BrainCell c = brain.cells[i];
                if ((c.active) && (c.connections.size() < 2*brain.resistance)) {
                    brain.connect(i, activeCell); --nConnections;
                }
            }
        }
    }
}

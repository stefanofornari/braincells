package com.github.stefanofornari.braincells;

/**
 *
 */
public class Brain {
    
    public final BrainCell[] cells;
    public final int resistance;
    
    /**
     * Create a Brain with the given number of cells and default resistance
     * @param size 
     */
    public Brain(int size) {
        this(size, 1);
    }
    
    public Brain(int size, int resistance) {
        cells = new BrainCell[size];
        for (int i=0; i<size; ++i) {
            cells[i] = new BrainCell();
        }
        this.resistance = resistance;
    }
    
    public Brain activate(int... positions) {
        for(int p: positions) {
            cells[p].active = true;
        }
        
        return this;
    }
    
    public Brain connect(int a, int b) {
        cells[a].connections.add(cells[b]);
        cells[b].connections.add(cells[a]);
        
        return this;
    }
    
    public Brain rest() {
        for(BrainCell c: cells) {
            c.active = false;
        }
        
        return this;
    }
    
    
    public void think() {
        //
        // we repeat the thinking until no new cells have been activated
        //
        boolean again = true;
        while (again) {
            again = false;
            for(BrainCell cell: cells) {
                int count = 0;
                for(BrainCell pair: cell.connections) {
                    if (pair.active) {
                        ++count;
                    }
                    if ((cell.active == false) && (count >= resistance)) {
                        again = cell.active = true;
                        break;
                    }
                }
            }
        }
    }
}

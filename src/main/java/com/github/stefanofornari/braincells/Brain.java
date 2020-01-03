package com.github.stefanofornari.braincells;

/**
 *
 */
public class Brain {
    
    public final BrainCell[] cells;
    
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
    }
    
    public Brain activate(int... positions) {
        for(int p: positions) {
            cells[p].active = true;
        }
        
        return this;
    }
    
    public Brain promoter(int a, int b) {
        cells[a].promoters.add(cells[b]);
        cells[b].promoters.add(cells[a]);
        
        return this;
    }
    
    public Brain detractor(int a, int b) {
        cells[a].detractors.add(cells[b]);
        cells[b].detractors.add(cells[a]);
        
        return this;
    }
    
    /**
     * De-activate all cells keeping all connections.
     * 
     * @return this brain
     */
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
                int potential = 0;
                for(BrainCell pair: cell.promoters) {
                    if (pair.active) {
                        ++potential;
                    }
                }
                for (BrainCell pair: cell.detractors) {
                    if (pair.active) {
                        --potential;
                    }
                }
                if ((potential > 0) && (!cell.active)) {
                    cell.active = true;
                    again = true;
                }
                if ((potential < 0) && cell.active) {
                    cell.active = false;
                    again = true;
                }
            }
        }
    }
}

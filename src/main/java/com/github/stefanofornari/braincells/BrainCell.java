package com.github.stefanofornari.braincells;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class BrainCell {
    public final Set<BrainCell> promoters = new HashSet<>();
    public final Set<BrainCell> detractors = new HashSet<>();
    
    public boolean active = false;

}

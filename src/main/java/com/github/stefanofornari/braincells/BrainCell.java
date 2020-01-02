package com.github.stefanofornari.braincells;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class BrainCell {
    public final Set<BrainCell> connections = new HashSet<>();
    
    public boolean active = false;

}

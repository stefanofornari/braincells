package com.github.stefanofornari.braincells;

import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class BugFreeBrainCells {
    
    public BugFreeBrainCells() {
    }
    
    @BeforeAll
    public static void beforeAll() {
    }
    
    @AfterAll
    public static void afterAll() {
    }
    
    @BeforeEach
    public void beforeEach() {
    }
    
    @AfterEach
    public void afterEach() {
    }

    @Test
    public void create_blank_brain_cells() {
        Brain brain = new Brain(1);
        
        then(brain.cells).hasSize(1);
        then(brain.cells[0].active).isFalse();
        
        brain = new Brain(10);
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c.active == false) &&
                       (c.connections.size() == 0);
            }
        });
    }
    
    @Test
    public void fluid_brain_connect() {
        Brain brain = new Brain(10);
        
        then(brain.connect(0,1)).isSameAs(brain);
    }
    
    @Test
    public void fluid_rest() {
        Brain brain = new Brain(10);
        
        then(brain.rest()).isSameAs(brain);
    }
    
    @Test
    public void rest_deactivate_all_cells() {
        Brain brain = new Brain(10);
        
        brain.activate(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).rest();
        
        thenBrainIsRested(brain);
    }
    
    @Test
    public void think_with_no_connections_does_not_activate_any_cell() {
        Brain brain = new Brain(10);
        
        brain.think();
        
        thenBrainIsRested(brain);
        
        brain.activate(0).think();
        
        thenBrainIs(
            brain,
            true, false, false, false, false, false, false, false, false, false
        );
        
        brain.activate(1, 2, 3, 4).think();
        
        thenBrainIs(
            brain,
            true, true, true, true, true, false, false, false, false, false
        );
    }
    
    @Test
    public void think_with_too_few_connections_does_not_activate_any_cell() {
        Brain brain = new Brain(10, 2);
        
        brain.connect(2, 0).connect(7, 5).connect(9, 2);
        
        brain.activate(2, 3, 5);
        
        brain.think();
        
        thenBrainIs(
            brain, 
            false, false, true, true, false, true, false, false, false, false
        );
    }
    
    @Test
    public void think_with_connections_over_resistence_activate_a_cell() {
        Brain brain = new Brain(10, 3);
        
        brain.connect(0, 1).connect(2, 1).connect(3, 1)
             .connect(5, 8).connect(5, 9)
             .connect(6, 3)
             .activate(0, 2, 3)
             .think();
        
        thenBrainIs(
            brain,
            true, true, true, true, false, false, false, false, false, false 
        );
    }
    
    @Test
    public void think_with_multiple_steps_activation() {
        Brain brain = new Brain(10, 2);
        
        brain.connect(0, 1).connect(1, 2).connect(1, 3)
             .connect(3, 4).connect(3, 5).connect(3, 6)
             .connect(6, 8).connect(7, 9)
             .activate(0, 5, 6)
             .think();
        
        thenBrainIs(
            brain,
            true, true, false, true, false, true, true, false, false, false 
        );
    }
    
    @Test
    public void brain_with_default_resistance() {
        fail("brain_with_default_resistance to be implemented!");
    }
    
    @Test
    public void connect_cells_ok() {
        fail("connect_cells_ok to be implemented!");
    }
    
    @Test 
    void connect_cells_with_wrong_ids() {
        fail("connect_cells_with_wrong_ids to be implemented!");
    }
    
    @Test
    void activate_cells_with_wrong_positions() {
        fail("activate_cells_with_wrong_positions to be implemented!");
    }
    
    @Test
    public void activate_cells() {
        Brain brain = new Brain(10, 2);
        
        brain.activate();
        thenBrainIsRested(brain);
        
        brain.activate(0, 2, 4, 6, 8);
        thenBrainIs(
            brain,
            true, false, true, false, true, false, true, false, true, false
        );
    }
    
    @Test
    public void fluid_activate() {
        Brain brain = new Brain(10, 2);
        
        then(brain.activate()).isSameAs(brain);
    }
    
    // --------------------------------------------------------- Private methods
    
    private Brain createBrain() {
        return new Brain(10);
    }
    
    private void thenBrainIsRested(Brain brain) {
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c.active == false);
            }
        });
    }
    
    private void thenBrainIs(Brain brain, boolean... cells) {
        for(int i=0; i<cells.length; ++i) {
            then(brain.cells[i].active).isEqualTo(cells[i]);
        }
    }
}
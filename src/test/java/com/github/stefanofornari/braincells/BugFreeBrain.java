package com.github.stefanofornari.braincells;

import static com.github.stefanofornari.braincells.Utils.thenBrainIs;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A brain is a set of BrainCells that may or may not be connected. A cell can
 * be active or not active and can be connected to one or more other cells. 
 * Given two connected cells A and B, the connection is defined promoting when
 * the activation of A promotes the activation of B (and vice versa - a 
 * connection is symmetric); when the activation of A inhibit the action of B, 
 * the connection is called detracting. A cell activates if and only if the
 * number of cells connected through promoting connections is greater than
 * the number of cells connected through detracting connections (in other words
 * if the number of active promoters is higher than the number of active 
 * detractors).
 * 
 * A brain has input and output cells. Input cells are directly activated 
 */
public class BugFreeBrain {
    
    public BugFreeBrain() {
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
    public void create_blank_brain() {
        Brain brain = new Brain(1);
        
        then(brain.cells).hasSize(1);
        then(brain.cells[0].active).isFalse();
        then(brain.cells[0].promoters).isEmpty();
        then(brain.cells[0].detractors).isEmpty();
        
        brain = new Brain(10);
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c.active == false) &&
                       c.promoters.isEmpty() &&
                       c.detractors.isEmpty();
            }
        });
    }
    
    @Test
    public void promoter_add_a_promoting_connection() {
        Brain brain = new Brain(5);
        
        brain.promoter(1,2);
        then(brain.cells[0].promoters).isEmpty();
        then(brain.cells[1].promoters).containsExactly(brain.cells[2]);
        then(brain.cells[2].promoters).containsExactly(brain.cells[1]);
        then(brain.cells[3].promoters).isEmpty();
        then(brain.cells[4].promoters).isEmpty();
        
        brain.promoter(3,4);
        then(brain.cells[0].promoters).isEmpty();
        then(brain.cells[1].promoters).containsExactly(brain.cells[2]);
        then(brain.cells[2].promoters).containsExactly(brain.cells[1]);
        then(brain.cells[3].promoters).containsExactly(brain.cells[4]);
        then(brain.cells[4].promoters).containsExactly(brain.cells[3]);
    }
    
    @Test
    public void fluid_brain_promoter() {
        Brain brain = new Brain(10);
        
        then(brain.promoter(0,1)).isSameAs(brain);
    }
    
    @Test
    public void detractor_add_a_detracting_connection() {
        Brain brain = new Brain(5);
        
        brain.detractor(1,2);
        then(brain.cells[0].detractors).isEmpty();
        then(brain.cells[1].detractors).containsExactly(brain.cells[2]);
        then(brain.cells[2].detractors).containsExactly(brain.cells[1]);
        then(brain.cells[3].detractors).isEmpty();
        then(brain.cells[4].detractors).isEmpty();
        
        brain.detractor(3,4);
        then(brain.cells[0].detractors).isEmpty();
        then(brain.cells[1].detractors).containsExactly(brain.cells[2]);
        then(brain.cells[2].detractors).containsExactly(brain.cells[1]);
        then(brain.cells[3].detractors).containsExactly(brain.cells[4]);
        then(brain.cells[4].detractors).containsExactly(brain.cells[3]);
    }
    
    @Test
    public void fluid_brain_detractor() {
        Brain brain = new Brain(10);
        
        then(brain.detractor(0,1)).isSameAs(brain);
    }
    
    @Test
    public void rest_deactivate_all_cells() {
        Brain brain = new Brain(10);
        
        brain.activate(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).rest();
        
        thenBrainIsRested(brain);
    }
    
    @Test
    public void fluid_rest() {
        Brain brain = new Brain(10);
        
        then(brain.rest()).isSameAs(brain);
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
    public void think_with_too_not_enough_promoters_does_not_activate_any_cells() {
        Brain brain = new Brain(10);
        
        brain.promoter(2, 0).promoter(7, 5).promoter(9, 2)
             .detractor(3, 0).detractor(5, 9).detractor(3, 7);
        
        brain.activate(2, 3, 5);
        
        brain.think();
        
        thenBrainIs(
            brain, 
            false, false, true, true, false, true, false, false, false, false
        );
    }
    
    @Test
    public void think_with_activate_a_cell_when_prmoteres_gt_detractrors() {
        Brain brain = new Brain(10);
        
        brain.promoter(0, 1).promoter(0, 4).promoter(4, 5).promoter(2, 5)
             .detractor(1, 3).detractor(2, 4)
             .activate(0, 2, 3)
             .think();
        
        thenBrainIs(
            brain,
            true, false, true, true, true, true, false, false, false, false 
        );
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
}
package com.github.stefanofornari.braincells;

import java.util.function.Predicate;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * R: activation resistance - number of connections that have to be risen to activate a cell
 * 
 * Learning algorithm:
 *   if cell is not active while expected to be active
 *     create R new connections to the first cells found active (itself excluded)
 *       which do not have 2*R connections 
 */
public class BugFreeBrainCellsLearning {
    
    public BugFreeBrainCellsLearning() {
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
            public boolean test(BrainCell t) {
                return (t.active == false) &&
                       (t.connections.size() == 0);
            }
        });
    }
    
    @Test
    public void do_nothing_if_target_matches() {
         Brain brain = createBrainAndThink(5); // we need at least one active cell as input
         BrainTrainer trainer = new BrainTrainer(brain);
         
        //
        // kick off the learning on same active cell 5
        //
        trainer.learn(5);
       
        //
        // no connections created
        //
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c.connections.isEmpty());
            }
        });
    }
    
    @Test
    public void create_connections_if_target_does_not_match_with_resistance_one() {
        
        //
        // FIRST
        //
        
        Brain brain = createBrainAndThink(0); // we need at least one active cell as input
        BrainTrainer trainer = new BrainTrainer(brain);
        
        //
        // we want cell 3 to be active
        //
        then(brain.cells[3].active).isFalse(); // just to make sure...
        
        //
        // kick off the learning
        //
        trainer.learn(3);
        
        //
        // we shall have only one connection, between cells 0 and 3
        //
        thenOnlyConnectionIs(brain, 3, 0);
        
        //
        // SECOND
        //
        
        brain = createBrainAndThink(9);
        trainer = new BrainTrainer(brain);
        
        //
        // we want cell 2 to be active
        //
        then(brain.cells[2].active).isFalse(); // just to make sure...
        
        //
        // kick off the learning
        //
        trainer.learn(2);
        
        //
        // we shall have only one connection, between cells 0 and 3
        //
        thenOnlyConnectionIs(brain, 2, 9);
    }
    
    @Test
    public void create_connections_if_target_does_not_match_with_resistance_three() {
        
        //
        // FIRST
        //
        
        Brain brain = createBrainAndThink(3, 0, 5, 8, 9);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        //
        // kick off the learning
        //
        trainer.learn(3);
        
        //
        // we shall have the three connections 3-0, 3-5, 3-8 (and, being resistance 3, not 3-9)
        //
        thenConnected(brain, 3, 0);
        thenConnected(brain, 3, 5);
        thenConnected(brain, 3, 8);
        thenNotConnected(brain, 3, 1);
        thenNotConnected(brain, 3, 2);
        thenNotConnected(brain, 3, 3);
        thenNotConnected(brain, 3, 4);
        thenNotConnected(brain, 3, 6);
        thenNotConnected(brain, 3, 7);
        thenNotConnected(brain, 3, 9);
    }
    
    @Test
    public void create_connections_avoiding_overconnected_cells() {
        //
        // A cell shall be considered for connection only if it is not already
        // connected to 2*resistence cells
        //
        Brain brain = createBrainAndThink(1, 2, 5);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        brain.connect(0, 2).connect(2,3);
        
        trainer.learn(7);
        
        
        thenConnected(brain, 7, 5);
        thenNotConnected(brain, 7, 0);
        thenNotConnected(brain, 7, 1);
        thenNotConnected(brain, 7, 2);
        thenNotConnected(brain, 7, 3);
        thenNotConnected(brain, 7, 4);
        thenNotConnected(brain, 7, 6);
        thenNotConnected(brain, 7, 7);
        thenNotConnected(brain, 7, 8);
        thenNotConnected(brain, 7, 9);
    }
    
    @Test
    public void learn() {
        //
        // cells: 10
        // R: 1
        // in: 1
        //
        // When 2 is active, 9 is active; when 1 is active, 5 is active
        //
        Brain brain = createBrainAndThink(1, 2);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        trainer.learn(9);
        
        brain.cells[2].active = false;
        brain.cells[1].active = true;
        
        brain.think(); trainer.learn(5);
        
        //
        // end of learning
        //
        brain.rest();
        
        brain.cells[2].active = true;
        brain.think();
        
        then(brain.cells[0].active).isFalse();
        then(brain.cells[1].active).isFalse();
        then(brain.cells[2].active).isTrue();
        then(brain.cells[3].active).isFalse();
        then(brain.cells[4].active).isFalse();
        then(brain.cells[5].active).isFalse();
        then(brain.cells[6].active).isFalse();
        then(brain.cells[7].active).isFalse();
        then(brain.cells[8].active).isFalse();
        then(brain.cells[9].active).isTrue();
        
    }
    
    // --------------------------------------------------------- Private methods
    
    private Brain createBrainAndThink(int resistance, int ... activeCells) {
        Brain brain = new Brain(10, resistance);
        
        for (int i=0; i<activeCells.length; ++i) {
            brain.cells[activeCells[i]].active = true;
        }
        
        brain.think();
        
        return brain;
    }
    
    private Brain createBrainAndThink(int activeCell) {
        return createBrainAndThink(1, activeCell);
    }
    
    private void thenOnlyConnectionIs(final Brain brain, int a, int b) {
        then(brain.cells[a].connections).containsOnly(brain.cells[b]);
        then(brain.cells[b].connections).containsOnly(brain.cells[a]);
        
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c == brain.cells[b]) ||
                       (c == brain.cells[a]) ||
                       (c.connections.isEmpty());
            }
        });
    }
    
    private void thenConnected(final Brain brain, int a, int b) {
        then(brain.cells[a].connections).contains(brain.cells[b]);
        then(brain.cells[b].connections).contains(brain.cells[a]);
    }
    
    private void thenNotConnected(final Brain brain, int a, int b) {
        then(brain.cells[a].connections).doesNotContain(brain.cells[b]);
        then(brain.cells[b].connections).doesNotContain(brain.cells[a]);
    }
}
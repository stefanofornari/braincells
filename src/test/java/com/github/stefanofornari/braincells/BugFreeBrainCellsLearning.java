package com.github.stefanofornari.braincells;

import static com.github.stefanofornari.braincells.Utils.thenBrainIs;
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
    public void do_nothing_if_target_matches() {
         Brain brain = createBrainAndThink(5); // we need at least one active cell as input
         BrainTrainer trainer = new BrainTrainer(brain);
         
        //
        // kick off the learning on same active cell 5
        //
        trainer.learn(new int[] {5}, new int[0]);
       
        //
        // no connections created
        //
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c.promoters.isEmpty() && c.detractors.isEmpty());
            }
        });
    }
    
    @Test
    public void create_promoters_if_target_shoud_be_active_and_it_is_not() {
        
        //
        // ONE
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
        trainer.learn(new int[] {3}, new int[0]);
        
        //
        // we shall have only one promoting connection, between cells 0 and 3
        //
        thenOnlyPromotingConnectionIs(brain, 3, 0);
        
        //
        // TWO
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
        trainer.learn(new int[] {2}, new int[0]);
        
        //
        // we shall have only one promoting connection, between cells 0 and 3
        //
        thenOnlyPromotingConnectionIs(brain, 2, 9);
        
        //
        // THREE
        //
        brain = createBrainAndThink(0, 9);
        trainer = new BrainTrainer(brain);
        
        //
        // kick off the learning
        //
        trainer.learn(new int[] {2, 3}, new int[0]);
        
        //
        // TODO: both 2 and 3 connect to 0 because it is the first active cell,
        // which then activates all promoted connections
        //
        
        thenPromoter(brain, 3, 0);
        thenPromoter(brain, 2, 0);
        thenNotConnected(brain, 0, 1);
        thenNotConnected(brain, 0, 4);
        thenNotConnected(brain, 0, 5);
        thenNotConnected(brain, 0, 6);
        thenNotConnected(brain, 0, 7);
        thenNotConnected(brain, 0, 8);
        thenNotConnected(brain, 0, 9);
        thenNotConnected(brain, 2, 1);
        thenNotConnected(brain, 2, 3);
        thenNotConnected(brain, 2, 4);
        thenNotConnected(brain, 2, 5);
        thenNotConnected(brain, 2, 6);
        thenNotConnected(brain, 2, 7);
        thenNotConnected(brain, 2, 8);
    }
    
    @Test
    public void create_detractors_if_target_shoud_be_inactive_and_it_is_not() {
        
        //
        // ONE
        //
        
        Brain brain = createBrainAndThink(0); // we need at least one active cell as input
        BrainTrainer trainer = new BrainTrainer(brain);
        
        //
        // we want cell 3 to be deactivated, therefore let's start from an
        // an active cell
        //
        brain.activate(3);
        
        //
        // kick off the learning
        //
        trainer.learn(new int[0], new int[] {3});
        
        //
        // we shall have only one detracting connection, between cells 0 and 3
        //
        thenOnlyDetractingConnectionIs(brain, 3, 0);
        
        //
        // TWO
        //
        
        brain = createBrainAndThink(9);
        trainer = new BrainTrainer(brain);
        
        //
        // we want cell 2 to be deactivated, therefore let's start from an
        // an active cell
        //
        brain.activate(2);
        
        //
        // kick off the learning
        //
        trainer.learn(new int[0], new int[] {2});
        
        //
        // we shall have only one promoting connection, between cells 0 and 3
        //
        thenOnlyDetractingConnectionIs(brain, 2, 9);
        
        //
        // THREE
        //
        brain = createBrainAndThink(0, 9);
        brain.activate(2).activate(3);
        trainer = new BrainTrainer(brain);
        
        //
        // kick off the learning
        //
        trainer.learn(new int[0], new int[] {2, 3});
        
        //
        // TODO: both 2 and 3 connect to 0 because it is the first active cell,
        // which then activates all promoted connections
        //
        
        thenDetractor(brain, 3, 0);
        thenDetractor(brain, 2, 0);
        thenNotConnected(brain, 0, 1);
        thenNotConnected(brain, 0, 4);
        thenNotConnected(brain, 0, 5);
        thenNotConnected(brain, 0, 6);
        thenNotConnected(brain, 0, 7);
        thenNotConnected(brain, 0, 8);
        thenNotConnected(brain, 0, 9);
        thenNotConnected(brain, 2, 1);
        thenNotConnected(brain, 2, 3);
        thenNotConnected(brain, 2, 4);
        thenNotConnected(brain, 2, 5);
        thenNotConnected(brain, 2, 6);
        thenNotConnected(brain, 2, 7);
        thenNotConnected(brain, 2, 8);
    }
    
    @Test
    public void create_connections_avoiding_overconnected_cells() {
        //
        // A cell shall be considered for connection only if it is not already
        // connected to 2*resistence cells
        //
        Brain brain = createBrainAndThink(1, 2, 5);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        brain.promoter(0, 2).promoter(2,3);
        
        trainer.learn(new int[] {7}, new int[0]);
        
        
        thenPromoter(brain, 7, 5);
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
    public void learn_10_1_1() {
        //
        // cells: 10
        // R: 1
        // in: 1
        //
        // Activations:
        // 2 -> 9, 1 -> 5
        //
        Brain brain = createBrainAndThink(1, 2);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        trainer.learn(new int[] {9}, new int[0]);
        
        brain.cells[2].active = false;
        brain.cells[1].active = true;
        
        brain.think(); trainer.learn(new int[] {5}, new int[0]);
        
        //
        // end of learning
        //
        brain.rest().activate(2).think();
        thenBrainIs(
            brain, 
            false, false, true, false, false, false, false, false, false, true
        );
        
        brain.rest().activate(1).think();
        thenBrainIs(
            brain, 
            false, true, false, false, false, true, false, false, false, false
        );
    }
    
    @Test
    public void learn_10_2_2() {
        //
        // cells: 10
        // R: 2
        // in: 2
        //
        // Activations:
        // (1,2) -> 7, (4,5) -> 9
        //
        Brain brain = createBrainAndThink(2, 1, 2);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        trainer.learn(new int[] {7}, new int[0]);
        
        brain.rest().activate(4, 5).think();
        
        trainer.learn(new int[] {9}, new int[0]);
        
        //
        // end of learning
        //
        
        brain.rest().activate(1).think();
        thenBrainIs(
            brain, 
            false, true, false, false, false, false, false, false, false, false
        );
        
        brain.rest().activate(1, 2).think();
        thenBrainIs(
            brain, 
            false, true, true, false, false, false, false, true, false, false
        );
        
        brain.rest().activate(4, 5).think();
        thenBrainIs(
            brain, 
            false, false, false, false, true, true, false, false, false, true
        );
    }
    
    @Test
    public void learn_10_2_4() {
        //
        // cells: 10
        // R: 2
        // in: 2
        //
        // Activations:
        // (1,2) -> 7, (4,5) -> 9, (1,2,4,5) -> (7,9,0)
        //
        Brain brain = createBrainAndThink(2, 1, 2, 4, 5);
        BrainTrainer trainer = new BrainTrainer(brain);
        
        trainer.learn(new int[] {7}, new int[0]);
        
        brain.rest().activate(4, 5).think();
        
        trainer.learn(new int[] {9}, new int[0]);
        
        //
        // end of learning
        //
        
        brain.rest().activate(1).think();
        thenBrainIs(
            brain, 
            false, true, false, false, false, false, false, false, false, false
        );
        
        brain.rest().activate(1, 2).think();
        thenBrainIs(
            brain, 
            false, true, true, false, false, false, false, true, false, false
        );
        
        brain.rest().activate(4, 5).think();
        thenBrainIs(
            brain, 
            false, false, false, false, true, true, false, false, false, true
        );
    }
    
    // --------------------------------------------------------- Private methods
    
    private Brain createBrainAndThink(int ... activeCells) {
        Brain brain = new Brain(10);
        
        for (int i=0; i<activeCells.length; ++i) {
            brain.cells[activeCells[i]].active = true;
        }
        
        brain.think();
        
        return brain;
    }
    
    private void thenOnlyPromotingConnectionIs(final Brain brain, int a, int b) {
        then(brain.cells[a].promoters).containsOnly(brain.cells[b]);
        then(brain.cells[a].detractors).isEmpty();
        then(brain.cells[b].promoters).containsOnly(brain.cells[a]);
        then(brain.cells[b].detractors).isEmpty();
        
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c == brain.cells[b]) ||
                       (c == brain.cells[a]) ||
                       (c.promoters.isEmpty() && c.detractors.isEmpty());
            }
        });
    }
    
    private void thenOnlyDetractingConnectionIs(final Brain brain, int a, int b) {
        then(brain.cells[a].detractors).containsOnly(brain.cells[b]);
        then(brain.cells[a].promoters).isEmpty();
        then(brain.cells[b].detractors).containsOnly(brain.cells[a]);
        then(brain.cells[b].promoters).isEmpty();
        
        then(brain.cells).allMatch(new Predicate<BrainCell>() {
            @Override
            public boolean test(BrainCell c) {
                return (c == brain.cells[b]) ||
                       (c == brain.cells[a]) ||
                       (c.promoters.isEmpty() && c.detractors.isEmpty());
            }
        });
    }
    
    private void thenPromoter(final Brain brain, int a, int b) {
        then(brain.cells[a].promoters).contains(brain.cells[b]);
        then(brain.cells[b].promoters).contains(brain.cells[a]);
    }
    
    private void thenDetractor(final Brain brain, int a, int b) {
        then(brain.cells[a].detractors).contains(brain.cells[b]);
        then(brain.cells[b].detractors).contains(brain.cells[a]);
    }
    
    private void thenNotConnected(final Brain brain, int a, int b) {
        then(brain.cells[a].promoters).doesNotContain(brain.cells[b]);
        then(brain.cells[b].promoters).doesNotContain(brain.cells[a]);
    }
}
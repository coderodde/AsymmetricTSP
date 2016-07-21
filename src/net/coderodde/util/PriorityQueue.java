package net.coderodde.util;

/**
 * This interface defines the API for priority queues.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jan 15, 2016)
 */
public interface PriorityQueue<E> {
    
    /**
     * This method attempts to add the element {@code element} to this priority
     * queue. If the input element is not this priority queue, it is inserted
     * in it and the method returns {@code true}. If the input element is 
     * already in this priority queue, it attempts to decrease the priority of 
     * that very element: if {@code priority} is smaller than the current
     * priority of the input element, it is decreased and {@code true} is
     * returned. If non of the previous cases apply, the state of the priority
     * queue remains intact and {@code false} is returned.
     * 
     * @param element  the element to insert/update.
     * @param priority the new priority.
     * @return {@code true} only if the structure of this priority queue has
     *         changed.
     */
    boolean add(E element, double priority);
    
    /**
     * Removes and returns the element with the highest priority.
     * 
     * @return the highest priority element.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    E extractMinimum();
    
    /**
     * Returns but does not remove the element with the highest priority.
     * 
     * @return the highest priority element.
     * @throws java.util.NoSuchElementException if the queue is empty.
     */
    E min();
    
    int size();
    
    boolean isEmpty();
}

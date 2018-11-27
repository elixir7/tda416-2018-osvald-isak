/**
 * Minimal implementation of a Sorted, Single linked Collection with an modified add and get method
 * @param <E> The type which should be stored in the list.
 * @author Isak Åslund
 * @version 2018
 */
public class SLCWithGet<E extends Comparable<? super E>>
                        extends LinkedCollection<E>
                        implements CollectionWithGet<E> {

    /**
     * Constructor of the sorted collection.
     * Only calls the parent class constructor which sets the variable head to null.
     */
    public SLCWithGet(){
        super();
    }

    /**
     * Adds an element to the sorted list regarding it's value. What is the value? Natural order or comparator?
     * @param e Elementt to add to the list
     * @return If the operation was successful or not
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean add(E e) {
        if(e == null){
            throw new NullPointerException();
        }

        //When the list is empty we need a special case.
        if(head == null){
            head = new Entry(e, null);
            return true;
        }

        Entry p = head;
        while(p != null){
            if(e.compareTo(p.element) > 0) {
                //The element is larger than the current
                //If p.next = null => add e after p
                //If e < p.next => add between (previous checked if p.next was null)
                //else just keep going.
                if(p.next == null){
                    p.next = new Entry(e, null);
                    break;
                }else if( e.compareTo(p.next.element) < 0){
                    p.next = new Entry(e, p.next);
                    break;
                }else{
                    p = p.next;
                }
            }else if(e.compareTo(p.element) == 0) {
                //Add a new entry after the first instance
                p.next = new Entry(e, p.next);
                break;
            }else if(e.compareTo(p.element) < 0){
                //Only the first comparison can lead to the case where e smaller than p
                head = new Entry(e, head);
                break;
            }
        }
        return true;
    }

    /**
     * Returns the first occurance of the elemt in the list.
     * @param e The element to find
     * @return The first occurance of e. If not found it returns null
     * @throws NullPointerException if the element supplied is null
     */
    @Override
    public E get(E e) {
        if (e == null){
            throw new NullPointerException();
        }

        Entry p = head;
        while(p != null){
            if(e.compareTo(p.element) == 0){
                return p.element;
            }
            p = p.next;
        }
        return null;
    }
}

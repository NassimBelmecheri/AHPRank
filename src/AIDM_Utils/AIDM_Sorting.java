package AIDM_Utils;

import java.util.HashMap;

import AIDM_Pattern.AIDM_Pattern;

public class AIDM_Sorting {
    node head = null;
    // node a, b;
    static class node {
    	AIDM_Pattern pattern;
        double val;
        
        node next;
 
        public node(AIDM_Pattern pattern,double val)
        {
        	this.pattern=pattern;
            this.val = val;
        }
    }
 
    node sortedMerge(node a, node b)
    {
        node result = null;
        /* Base cases */
        if (a == null)
            return b;
        if (b == null)
            return a;
 
        /* Pick either a or b, and recur */
        if (a.val <= b.val) {
            result = a;
            result.next = sortedMerge(a.next, b);
        }
        else {
            result = b;
            result.next = sortedMerge(a, b.next);
        }
        return result;
    }
 
    node mergeSort(node h)
    {
        // Base case : if head is null
        if (h == null || h.next == null) {
            return h;
        }
 
        // get the middle of the list
        node middle = getMiddle(h);
        node nextofmiddle = middle.next;
 
        // set the next of middle node to null
        middle.next = null;
 
        // Apply mergeSort on left list
        node left = mergeSort(h);
 
        // Apply mergeSort on right list
        node right = mergeSort(nextofmiddle);
 
        // Merge the left and right lists
        node sortedlist = sortedMerge(left, right);
        return sortedlist;
    }
 
    // Utility function to get the middle of the linked list
    public static node getMiddle(node head)
    {
        if (head == null)
            return head;
 
        node slow = head, fast = head;
 
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
 
    void push(AIDM_Pattern pattern, double new_data)
    {
        /* allocate node */
        node new_node = new node(pattern ,new_data);
 
        /* link the old list off the new node */
        new_node.next = head;
 
        /* move the head to point to the new node */
        head = new_node;
    }
 
    // Utility function to print the linked list
    void printList(node headref)
    {
        while (headref != null) {
            System.out.print(headref.val + " ");
            headref = headref.next;
        }
    }
    HashMap<AIDM_Pattern, Double> getSortedMap(node headref)
    {
    	HashMap<AIDM_Pattern, Double> hm = new HashMap<AIDM_Pattern, Double>();
        while (headref != null) {
        	hm.put(headref.pattern, headref.val);
            headref = headref.next;
        }
		return hm;
    }
   
}
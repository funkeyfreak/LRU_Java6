import java.util.HashMap;
import java.util.Scanner;



/**
 * This is the LRU Coding example as giving by BrandVarity
 * 
 * @author Dalin Williams
 * @version 1.0.0
 */


public class LRU {
	/**
	 * This hashmap holds a combination of string elements and our DoubleLinkedList nodes
	 * 
	 * NOTE: I am not using static, as the problem states "... your cache does not need persistence across sessions."
	 */
	private HashMap<String, Node> map;
	/**
	 * The integer assoiated with the size of our hash map / LRU cache
	 */
	private int size;
	/**
	 * The head of doubly linked list
	 */
	private Node head;
	/**
	 * The tail of our doubly linked list
	 */
	private Node tail;
	
	public LRU(int size){
		this.head = null;
		this.tail = null;
		
		if(size < 1){
			size = 64;
		}		
		this.size = size;
		this.map = new HashMap<String,Node>();
	}
	
	/**
	 * A simple linked list implementation to ease the LRUI-ness of this application. This Link List will allow us to easly ease out 'old' data
	 * 
	 * NOTE: We did not include any defalt type checking as one would do in an official release of a LRU product
	 * @author Dalin Williams
	 *
	 */
	private class Node {
		//The key within this node, in essence treating this as an element within our map
		private String key;		
		public void setKey(String newKey){
			
			this.key = newKey;
			return;
		}		
		public String getKey(){
			return this.key;
		}
		//The value within this node, in essence treating this as an element within our map
		private String value;		
		public void setValue(String newValue){
			this.value = newValue;
			return;
		}	
		public String getValue(){
			return this.value;
		}
		//This is the 'pointer' to the next node
		private Node next;
		public void setNext(Node newNext){
			this.next = newNext;
			return;
		}
		public Node getNext(){
			return this.next;
		}		
		//This is the 'pointer' to the previous node
		private Node prev;
		public void setPrev(Node newPrev){
			this.prev = newPrev;
			return;
		}
		public Node getPrev(){
			return this.prev;
		}
		
		public Node(String key, String value, Node next, Node prev){
			this.key = key;
			this.value = value;
			this.next = next;
			this.prev = prev;
		}
		
		
		public Node(String key, String value){
			this.key = key;
			this.value = value;
			this.next = null;
			this.prev = null;
		}
		
			
	}
	
	/**
	 * This function moves a node to the front of our linked list
	 * @param element The node to move to front
	 * @return VOID
	 */
	private void moveToFront(Node element){
		//A simple check to see if we are already done with our move
		if(element == head || element == null){
			return;
		}
		//temporary next and prev nodes of this element being moved
		Node next = element.getNext();
		Node prev = element.getPrev();
		
		//set the next and prev if they exist
		if(next != null){
			next.setPrev(element.getPrev());
		}
		if(prev != null){
			prev.setNext(element.getNext());
		}
		
		//set this new head's nodes
		element.setNext(null);
		element.setPrev(head);

		/*Node t = head;
		Node hNext = head.getNext();
		Node hPrev = head.getPrev();
		
		head = element;
				
		if(t != null){
			if(hNext != null){
				t = 
			}
		}*/
		
		
		//if head is not null, we become its previous
		if(head != null){
			head.setNext(element);
		}
		
		head = element;
		
		//if the tail element is ourselves, we become its previous
		if(tail == element){
			tail = next;
		}
		
		return;
	}
	
	/**
	 * This function sets a key value pair into our cache
	 *  
	 * @param key String The new key
	 * @param Value String The new value
	 * @return VOID
	 */
	public void set(String key, String value) {
		//create a storage node, this will simply help with the generalization of my code	
		Node element = map.get(key); 
		
		//if we do not already have this element, set a new 
		if(element == null){
			element = new Node(key, value);			
			
			//we first check the size. If the size is surpassed or equal, we perform an LRU update
			if(map.size() >= size){
				//remove the element from our map
				map.remove(tail.getKey());
				//configure the tail
				tail = tail.getNext();
				if(tail != null){
					tail.setPrev(null);
				}
			}
			//and add the element
			map.put(key, element);
		}
		
		//if our tail is not set, SET IT NOW (This will initialize our tail on our first set)
		if(tail == null){
			tail = head;
		}
		
		//we set the value of our node here, so if this entry already exists within our map, we do this operation and boom, we are complete
		element.setValue(value);
		//In the end, we always move to the front :D	
		moveToFront(element);
		
		return;
	}
	
	
	/**
	 * This function gets a value from our cache if it exists, otherwise it returns an empty string 
	 * @param key String The to find within our cache
	 * @return If key.exist ? value : ""
	 */
	public String get(String key){
		//create a storage node, this will simply help with the generalization of my code	
		Node element = map.get(key); 
				
		//A simple check to see if the map is empty or not
		if(map.isEmpty()){
			return "";
		}
		//if it is not empty, we go ahead and see if the map contains our value
		if(element != null){
			//move the element to the front
			moveToFront(element);
			//and return the value
			return element.getValue();
		}
		//if the element is not found, we exit with empty string
		return "";		
	}
	
	public static void main(String [] args){
		String input;
		LRU cache = null;
		Boolean exit = false;
		Scanner scanIn = new Scanner(System.in);		
		
		while(!exit){
		    // I had to look this one up http://www.mkyong.com/java/how-to-read-input-from-console-java/
	        input = scanIn.nextLine();
	        
	       
			if(input != ""){
				String[] inputSplit = input.split(" ");
				String operation = inputSplit[0];
				String results = "ERROR";
				int length = inputSplit.length;
				
				try{
					if(operation.equals("SIZE")){
						if(length == 2 && cache == null){
							int size = Integer.parseInt(inputSplit[1]);
							cache = new LRU(size);
							results = "SIZE OK";
						}
					}
					else if(operation.equals("SET")){
						if(length == 3 && cache != null){
							cache.set(inputSplit[1], inputSplit[2]);
							results = "SET OK";
						}
					}
					else if(operation.equals("GET")){
						if(length == 2 && cache != null){
							String key = inputSplit[1];
							results = cache.get(key);
							if(results == ""){
								results = "NOTFOUND";
							}
							else{
								results = "GOT " + results;
							}
						}
					}
					else if(operation.equals("EXIT")){
						exit = true;
						results = "";
					}
				}
				finally{
					System.out.println(results);
				}
			}
		}
		scanIn.close();
	}
}

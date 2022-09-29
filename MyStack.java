import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class StackEmpty extends Exception{  StackEmpty() {super("the stack is empty"); } }
class Stackfull extends Exception{ Stackfull() { super("the stack is full"); } }

interface IStack {
  
  /*** Removes the element at the top of stack and returnsthat element.
  * @return top of stack element, or through exception if empty
  */
  
  public Object pop()throws StackEmpty;
  
  /*** Get the element at the top of stack without removing it from stack.
  * @return top of stack element, or through exception if empty
  */
  
  public Object peek()throws StackEmpty;
  
  /*** Pushes an item onto the top of this stack.
  * @param object to insert*
  */
  
  public void push(Object element);
  
  /*** Tests if this stack is empty
  * @return true if stack empty
  */
  public boolean isEmpty();
  
  public int size();
}


public class MyStack implements IStack {
  class Node {
    private Object item;
    private Node next;
    public Object getItem() { return item; }
    public void setItem(Object item) { this.item = item; }
    public Node getNext() { return next; }
    public void setNext(Node next) { this.next = next; }
  }
  private Node top = null;
  private int size = 0;
  //[1]
  public Object pop()throws StackEmpty{
    if(top == null)throw new StackEmpty();
    else{
        Object val = top.getItem();
        if(size != 1)top = top.getNext();
        else top = null;
        size--;
        return val;
    }
  }

  //[2]
  public void push(Object element){
    Node newnode = new Node();
    newnode.setItem(element);
    newnode.setNext(top);
    top = newnode;
    size++;
  }

  //[3]
  public Object peek()throws StackEmpty{
    if(top == null)  throw new StackEmpty();
    else { return top.getItem(); }
  }

  public static void printst(MyStack prst){
    System.out.print("[");
    int vol = prst.size();
    try{
      for(int i = 0; i < vol; i++){
        if(i == vol - 1)System.out.print(prst.pop());
        else { System.out.print(prst.pop() + ", "); }
      }
      System.out.print("]");
    }catch(Exception e){  }

  }

  //[4]
  public int size(){ return size; }
  public boolean isEmpty(){ return (top == null); }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    String getinp = input.nextLine().replaceAll("\\[|\\]", "");
    MyStack st = new MyStack();
    if(getinp.length() != 0){
      String[] separ_val = getinp.split(", ");
      for(int i = 0; i < separ_val.length; i++){
        st.push(separ_val[separ_val.length - 1 - i]);
      }
    }
    
    try{
      switch(input.nextLine()){
        case "pop":
          st.pop();
          printst(st);
        break;
        case "peek":
          System.out.println(st.peek());
        break;
        case "push":
          st.push(input.nextInt());
          printst(st);
        break;
        case "size":
          System.out.println(st.size());
        break;
        case "isEmpty":
          if(st.isEmpty())System.out.println("True");
          else System.out.println("False");
        break;
        default:
          throw new InputMismatchException();
      }
    }catch(Exception e){ System.out.println("Error"); }
    
  }
}


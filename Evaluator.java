import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
  
/**
* Takes a symbolic/numeric infix expression as input and converts it to
* postfix notation. There is no assumption on spaces between terms or the
* length of the term (e.g., two digits symbolic or numeric term)
*
* @param expression infix expression
* @return postfix expression
*/
  
public String infixToPostfix(String expression)throws Exception;
  
  
/**
* Evaluate a postfix numeric expression, with a single space separator
* @param expression postfix expression
* @return the expression evaluated value
*/
  
public int evaluate(String expression)throws Exception;

}


public class Evaluator implements IExpressionEvaluator {



  public String infixToPostfix(String expression)throws Exception{
    //edit
    expression = editMins(expression);
    expression = UnaryOp(expression);
    if( ! checkvalidity(expression) )throw new Exception();
    String postfixExp = "";
    MyStack<Character> st = new MyStack<Character>();
    MyStack<Character> tempst = new MyStack<Character>();
      for(int i = 0; i < expression.length(); i++){
        char ind_ch = expression.charAt(i);
        //case [1]
        if(Character.isLetter(ind_ch))postfixExp += ind_ch;
        
        //case [2]
        else if(ind_ch == '(')st.push(ind_ch);
        
        //case [3]
        else if( isoper(ind_ch) ){

          if(st.isEmpty())st.push(ind_ch);
          else if( isoper(st.peek()) ){ 
            
            //case [3 - 1]
            if( precLevel( ind_ch ) > precLevel( st.peek() )) { st.push(ind_ch); }
            
            //case [3 - 2]
            else if( precLevel( ind_ch ) <= precLevel( st.peek() ) ){
              postfixExp += st.pop(); 
              while(!st.isEmpty() && st.peek() != '(' && precLevel( ind_ch ) <= precLevel( st.peek() )){
                postfixExp += st.pop();
              }
              st.push(ind_ch);
              
            }
          }
          else if( st.peek() == '(' )st.push(ind_ch);  
        }

        //case [4]
        else if(ind_ch == ')' ){
          while( !st.isEmpty() && st.peek() != '('){ postfixExp += st.pop(); }
          if(st.isEmpty()) throw new Exception();
          else st.pop();
        }
        


      }
      while( !st.isEmpty() ){ 
        if( st.peek() != '(' ) postfixExp += st.pop();
        else throw new Exception(); 
      }
    return postfixExp;
}



  public int evaluate(String expression)throws Exception{ 
    MyStack<Integer> st = new MyStack<Integer>();
    for(int i = 0; i < expression.length(); i++){

      char ind_ch = expression.charAt(i);
      
      if(ind_ch == ' ')continue;
      else if(ind_ch == '-' && Character.isDigit(expression.charAt(i+1)) ){
        int j = i + 2;
        while(expression.charAt(j) != ' '){
          j++;
        }
        st.push( Integer.parseInt( expression.substring(i, j) ) );
        i = j - 1;
      }
      else if( Character.isDigit( ind_ch ) ){
        int j = i + 1;
        while(expression.charAt(j) != ' '){
          j++;
        }
        st.push( Integer.parseInt( expression.substring(i, j) ) );
        i = j - 1;
      } else {
        int val2 = st.pop();
        int val1 = st.pop();
        Object res = Calc(val1, val2, ind_ch );
        st.push( (Integer)res );
      }
    }
    
    return st.pop();
  }

  /**
  * getting precedence level of operators in maths
  * @param op : operator we want to get its preclevel
  * @return prec : precedence level of operators in maths
  */
  

  int precLevel(char op) {
      int prec = -1 ;
      if(isoper(op)){
        switch (op) {
          case '+':
          case '-':
              prec = 0;
          break;
          case '*':
          case '/':
              prec = 1;
          break;
          case '^':
              prec = 2;
          break;
        }
      }else System.out.println("error preclevel");
      
    return prec;
  }
  boolean isoper(char op){
    return (op == '+' || op == '-' || op == '*' || op == '/' || op == '^');
  }


    /**
  * convert '--' expression into '+' or ''
  * @param exp arithmetic expression
  * @return exp expression after edit
  */

  String editMins(String exp){
    for(int i = 0; i < exp.length() - 2; i++){
      if(i == 0 && exp.charAt(i) == '-' && exp.charAt(i+1) == '-')exp = exp.substring(i+2);
      else if(exp.charAt(i) == '-' && exp.charAt(i+1) == '-' && Character.isLetter(exp.charAt(i-1)) ) exp = exp.substring(0, i) + "+" + exp.substring(i+2);
      else if(exp.charAt(i) == '-' && exp.charAt(i+1) == '-' && (exp.charAt(i-1) =='(' || isoper(exp.charAt(i-1)))) exp = exp.substring(0, i) + exp.substring(i+2); 
    }
    return exp;
  }
  
  /**
  * dealing with unary operators by convert -a into x-a
  * @param exp arithmetic expression
  * @return exp expression after editing
  */
  
  //edit more if you want it to be more efficient
  String UnaryOp(String exp){
    int len = exp.length();
    for(int i = 0; i < len - 1; i++){
      if(i == 0 && exp.charAt(i) == '-' && ( exp.charAt(i+1) == '(' || Character.isLetter( exp.charAt(i+1) ) ) ){ exp = 'x' + exp; len++; }
      else if(exp.charAt(i) == '-' && exp.charAt(i-1) == '(' && Character.isLetter( exp.charAt(i+1) ) ){ exp = exp.substring(0, i) + 'x' + exp.substring(i); len++; }
      // else if( exp.charAt(i) == '-' && isoper(exp.charAt(i-1)) ){
      //   if( Character.isLetter( exp.charAt(i+1) ) ) { exp = exp.substring(0, i) + "(x" + exp.substring(i, i+2) + ")" + exp.substring(i+2, len); }
      //   //else if()
      // }
    }
    return exp;
  }

  /**
  * check the validity of expression according to letters in it and unvalid operators position
  * @param exp arithmetic expression
  * @return boolean expression true if valid , false if not.
  */


  //edit more if you want it to be more efficient
  boolean checkvalidity(String exp){
    char[] expr = exp.toCharArray();
    for(int i = 0; i < exp.length() - 1; i++){
      if(Character.isLetter(expr[i]) && Character.isLetter(expr[i+1]) ){ return false; }
    }
    if( isoper( expr[exp.length() - 1] ) ){ return false; }
    else if( isoper( expr[0] ) ) return false;
    for(int i = 0; i < expr.length - 1 ; i++){
      char ch1 = expr[i] , ch2 = expr[i+1];
      if(Character.isLetter(ch1) || Character.isLetter(ch2) || ch1 == '(' || ch1== ')' || ch2 == '(' || ch2 == ')')continue;
      else if((precLevel(ch1) == 1 || precLevel(ch1) == 2) && (precLevel(ch2) == 1 || precLevel(ch2) == 2)){return false;}
      else if( (precLevel(ch1) == 1 || precLevel(ch1) == 0) && (precLevel(ch2) == 1 || precLevel(ch2) == 0) ){ return false; }

    }
    return (true);
  }


  /**
  * calculate value resulting from applying operator on two int operand variables
  * @param x first operand , y second operand and op the operator
  * @return calculated value if exists
  */




    Object Calc(int x , int y , char op){
      int value = 0;      
      switch(op){
        case '+':
          value =  (x + y);
        break;
        case '-':
          value = (x - y);
        break;
        case '*':
          value = (x * y);
        break;
        case '/': 
            value = x/y;//assume that y != 0
        break;
        case '^':
          value = (int)Math.pow(x, y);
        break;
      }
      return value;
    }


    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      Evaluator E = new Evaluator();
      String prefixExp = input.nextLine();
      //////if not exists
      String A_st = input.nextLine().substring(2);
      String B_st = input.nextLine().substring(2);
      String C_st = input.nextLine().substring(2);
      try{
        String postfixExp = E.infixToPostfix(prefixExp);
        String printed_postfixExp = postfixExp.replaceAll("x", ""); 
        System.out.println(printed_postfixExp);
        int len = postfixExp.length() * 2;
        for(int i = 0; i < len; i += 2){
          // System.out.println(i);
          postfixExp = postfixExp.substring(0, i+1) + " " + postfixExp.substring(i+1);
        }
        // System.out.println(postfixExp);
        int a = Integer.parseInt(A_st);
        int b = Integer.parseInt(B_st);
        int c = Integer.parseInt(C_st);
        postfixExp = postfixExp.replaceAll("a", Integer.toString(a));
        postfixExp = postfixExp.replaceAll("b", Integer.toString(b));
        postfixExp = postfixExp.replaceAll("c", Integer.toString(c));
        postfixExp = postfixExp.replaceAll("x", "0");
        System.out.println(E.evaluate(postfixExp));

      }catch(Exception e){System.out.println("Error");}
      
    }
}


  /**
  * my implemented linked-based stack class
  * include node class with its getters and setters
  * include pop , peek , push , size and isEmpty functions.
  */

class MyStack<reqty>{
    class Node {
      private reqty item;
      private Node next;
      public reqty getItem() { return item; }
      public void setItem(reqty item) { this.item = item; }
      public Node getNext() { return next; }
      public void setNext(Node next) { this.next = next; }
    }
    private Node top = null;
    private int size = 0;
    //[1]
    public reqty pop(){
      if(top == null){ System.out.println("Error pop"); return null; }
      else{
          reqty val = top.getItem();
          if(size != 1)top = top.getNext();
          else top = null;
          size--;
          return val;
      }
    }
  
    //[2]
    public void push(reqty element){
      Node newnode = new Node();
      newnode.setItem(element);
      newnode.setNext(top);
      top = newnode;
      size++;
    }
  
    //[3]
    public reqty peek(){
      if(top == null) { System.out.println("error peek"); return null; }
      else { return top.getItem(); }
    }
  
    //[4]
    public int size(){ return size; }
    public boolean isEmpty(){ return (top == null); }
  }
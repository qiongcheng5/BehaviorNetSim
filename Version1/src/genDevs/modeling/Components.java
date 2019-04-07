package genDevs.modeling;

import GenCol.*;

import java.util.*;


/*
class componentIterator{
private Iterator it;
public componentIterator(ComponentsInterface c){it = c.iterator();}
public boolean hasNext(){return it.hasNext();}
public IOBasicDevs nextComponent() {return (IOBasicDevs)it.next();}   //  ???  Problem
}
*/

//public class Components extends threadEnsembleSet implements ComponentsInterface{
public class Components extends ensembleSet implements ComponentsInterface{
public componentIterator cIterator(){
return new componentIterator(this);
}
}
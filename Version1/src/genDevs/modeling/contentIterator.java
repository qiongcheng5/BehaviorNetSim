package genDevs.modeling;

import GenCol.*;
import java.util.*;

class contentIterator implements ContentIteratorInterface{
private Iterator it;
public contentIterator(MessageInterface m){it = m.iterator();}
public boolean hasNext(){return it.hasNext();}
public ContentInterface next() {return (ContentInterface)it.next();}

}
package GenCol;

import java.util.*;

interface ensembleCollection extends ensembleBasic, Collection{
public void print();
public  void wrapAll(ensembleInterface Result,Class cl);
public ensembleInterface copy(ensembleInterface ce);
}


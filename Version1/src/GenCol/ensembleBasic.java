package GenCol;

import java.util.*;

interface ensembleBasic {
public void tellAll(String MethodNm,Class[] classes,Object[] args);
public void tellAll(String MethodNm);
public void AskAll(ensembleInterface result,String MethodNm,Class[] classes,Object[] args);
public void which(ensembleInterface result,String MethodNm,Class[] classes,Object[] args);
public Object whichOne(String MethodNm,Class[] classes,Object[] args);
}


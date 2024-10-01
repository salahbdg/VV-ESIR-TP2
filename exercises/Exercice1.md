# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer
Tight Class Cohesion and Loose Class Cohesion metrics produce the same value for a given Java Class when  there is all method pairs that are indirectly connected are directly connected, which means when a method access to an attribute, it must always share it  with other methods that also access it, eliminating  the possibility indirect connections.

There's an example of methods directly connected : 

```Java
class ExampleDirectlyConnected{  
private int att1;
private int att2;
public int getAtt1(){return att1;}
public void setAtt1(int att1){this.att1 = att1;}
}
```
There's an example of methods indirectly connected :   


```Java
class ExampleIndirectlyConnected{
private int att1;
private int att2;
private int att3;
public int getAtt1(){return att1;}
public int getAtt2(){return att2;}
public void setAtt1(int att1){this.att1 = att1;}
public void setAtt2(int att2){this.att2 = att2;}
public int concatAtt12(){return att1 + att2;}
public int concatAtt23(){return att2 + att3;} // here we have like an indirect connection with concatAtt12() because both of them use att2 so they are indirectly connected because of them share the methods of the attribute att2

}
```



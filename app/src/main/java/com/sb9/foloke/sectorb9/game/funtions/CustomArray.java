package com.sb9.foloke.sectorb9.game.funtions;
import java.time.temporal.*;

public class CustomArray<T>
{
	public static class CustomElement<T>
	{
		protected CustomElement next;
		//protected CustomElement self;
		protected T data;
		public CustomElement(T elem)
		{
			next=null;
			data=elem;
		}
	}
	//private T array[];
	private T stackPtr;                      // указатель на стек
    private int size;                   // максимальное количество элементов в стеке
    CustomElement bottom;      
	CustomElement top;// номер текущего элемента стека
	//public Stack(int = 10);                  // по умолчанию размер стека равен 10 элементам
    //Stack(const Stack<T> &);          // конструктор копирования
    //~Stack();                         // деструктор

    //inline void push(const T & );     // поместить элемент в вершину стека
    //inline T pop();                   // удалить элемент из вершины стека и вернуть его
    //inline void printStack();         // вывод стека на экран
    //inline const T &Peek(int ) const; // n-й элемент от вершины стека
    //inline int getStackSize() const;  // получить размер стека
   // inline T *getPtr() const;         // получить указатель на стек
   // inline int getTop() const;        // получить номер текущего элемента в стеке

 
// реализация методов шаблона класса STack
 
// конструктор Стека

public CustomArray()
{
    size=0; // инициализация константы
   // stackPtr = new T[size]; // выделить память под стек
    bottom = null; // инициализируем текущий элемент нулем;
	stackPtr=null;
	top=null;
}
 
// конструктор копирования


 
// функция деструктора Стека

 
// функция добавления элемента в стек

public void push(T value)
{
	if(size==0)
	{
		bottom=new CustomElement(value);
		top=bottom;
	}
		else
			{
				top.next=new CustomElement(value);
				top=top.next;
			}
	size++;
	
    // проверяем размер стека.nex
    //assert(top < size); // номер текущего элемента должен быть меньше размера стека
 
   // stackPtr[top++] = value; // помещаем элемент в стек
}
 
// функция удаления элемента из стека
public boolean pop()
{
    // проверяем размер стека
   // assert(top > 0); // номер текущего элемента должен быть больше 0
 if ((size)>0)
 {
	 CustomElement tbot=bottom.next;
	 bottom=tbot;
	 size--;
	 return true;
 }
   return false;
}
 
/*public T[] getArray()
{
	T tarray[]=new T[size];
	return tarray;
}*/
}

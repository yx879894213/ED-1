package ule.edi.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ule.edi.model.*;
import ule.edi.model.Configuration.Type;


public class EventArrayImpl implements Event {
	
	private String name;
	private Date eventDate;
	private int nSeats;
	
	private Double price;    // precio de entradas 
	private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)
   	
	private Seat[] seats;
	
	
	
	
   public EventArrayImpl(String name, Date date, int nSeats){
	 //TODO 
	 // utiliza los precios por defecto: DEFAULT_PRICE y DEFAULT_DISCOUNT definidos en Configuration.java   
	 // Debe crear el array de butacas
	   
	   this.name=name;
	   this.eventDate=date;
	   this.nSeats=nSeats;
	   this.price=Configuration.DEFAULT_PRICE;
	   this.discountAdvanceSale=Configuration.DEFAULT_DISCOUNT;
	   seats = new Seat[this.nSeats];
   }
   
   
   public EventArrayImpl(String name, Date date, int nSeats, Double price, Byte discount){
	   //TODO 
	   // Debe crear el array de butacas
	   this.name=name;
	   this.eventDate=date;
	   this.nSeats=nSeats;
	   this.price=price;
	   this.discountAdvanceSale=discount;
	   seats = new Seat[this.nSeats];
	  
   }


@Override
public String getName() {
	// TODO Auto-generated method stub
	return this.name;
}


@Override
public Date getDateEvent() {
	// TODO Auto-generated method stub
	return this.eventDate;
}


@Override
public Double getPrice() {
	// TODO Auto-generated method stub
	return this.price;
}


@Override
public Byte getDiscountAdvanceSale() {
	// TODO Auto-generated method stub
	return this.discountAdvanceSale;
}


@Override
public int getNumberOfSoldSeats() {
	// TODO Auto-generated method stub
	
	int libre=0;
	for(int i=0;i<seats.length;i++) 
		if(seats[i]==null)
			libre++;
		
	return seats.length-libre;
}


@Override
public int getNumberOfNormalSaleSeats() {
	// TODO Auto-generated method stub
	int norm=0;
	
	for(int i=0; i<seats.length; i++) 
		if(seats[i]!=null && seats[i].getType()==Configuration.Type.NORMAL) 
			norm++;
		
	return norm;
}

@Override
public int getNumberOfAdvanceSaleSeats() {
	// TODO Auto-generated method stub
	
	return getNumberOfSoldSeats()-getNumberOfNormalSaleSeats();
}


@Override
public int getNumberOfSeats() {
	// TODO Auto-generated method stub
	return this.nSeats;
}


@Override
public int getNumberOfAvailableSeats() {
	// TODO Auto-generated method stub
	return this.nSeats-getNumberOfSoldSeats();
}


@Override
public Seat getSeat(int pos) {
	// TODO Auto-generated method stub
	if(pos<1 || pos>seats.length) {
		return null;
	}else {
	return seats[pos-1];
	}
}


@Override
public Person refundSeat(int pos) {
	// TODO Auto-generated method stub
	if(pos<1 || pos>seats.length) {
		return null;
	}
	Person holder=seats[pos-1].getHolder();
	seats[pos-1]=null;
	return holder;


}


@Override
public boolean sellSeat(int pos, Person p, boolean advanceSale) {
	// TODO Auto-generated method stub
	if(pos<1 || pos > seats.length || seats[pos-1]!=null) {
		return false;
	}
	
	Type type ;
	if(advanceSale==false) 
		type=Type.NORMAL;
	else
		type=Type.ADVANCE_SALE;
	
	seats[pos-1]=new Seat(this, pos-1, type, p);
	
	return true;
}


@Override
public int getNumberOfAttendingChildren() {
	// TODO Auto-generated method stub
	int ninos=0;
	
	for(int i=0; i<seats.length; i++) {
		if(seats[i]!=null && seats[i].getHolder().getAge()<Configuration.CHILDREN_EXMAX_AGE) {
			ninos++;
		}
	}
	
	return ninos;
}


@Override
public int getNumberOfAttendingAdults() {
	// TODO Auto-generated method stub
	int adultos=0;
	
	for(int i=0; i<seats.length; i++) {
		if( seats[i]!=null && 
			seats[i].getHolder().getAge()>=Configuration.CHILDREN_EXMAX_AGE &&
			seats[i].getHolder().getAge()<Configuration.ELDERLY_PERSON_INMIN_AGE) {
		
			adultos++;
		}
	}
	
	return adultos;
}


@Override
public int getNumberOfAttendingElderlyPeople() {
	// TODO Auto-generated method stub
	int ancianos=0;
	
	for(int i=0; i<seats.length; i++) {
		if(seats[i]!=null &&	seats[i].getHolder().getAge()>=Configuration.ELDERLY_PERSON_INMIN_AGE) {
		ancianos++;
		}
	}
	
	return ancianos;
}


@Override
public List<Integer> getAvailableSeatsList() {
	// TODO Auto-generated method stub
	
	ArrayList<Integer> lista = new ArrayList<Integer>();
	
	for(int i=0; i<seats.length; i++) {
		if(seats[i]==null) 
			lista.add(i+1);
		
	}
	
	return lista;
}


@Override
public List<Integer> getAdvanceSaleSeatsList() {
	// TODO Auto-generated method stub
	
	List<Integer> lista = new ArrayList<Integer>();
	
	
	for(int i=0; i<seats.length; i++) {
		if(seats[i]!=null && seats[i].getType()==Type.ADVANCE_SALE) {
			lista.add(i+1);
		}
	}
	
	return lista;
}


@Override
public int getMaxNumberConsecutiveSeats() {
	// TODO Auto-generated method stub
	int maxNum=0;
	int temporar;
	
	for(int i=0; i<seats.length;i++) {
		temporar=1;
		
		while(seats[i++]==null && i<seats.length) 
			temporar++;
		
		if(temporar>maxNum) 
			maxNum=temporar;
		
	}
	
	return maxNum;
}


@Override
public Double getPrice(Seat seat) {
	// TODO Auto-generated method stub

	if (seat==null) 
		return 0.00;		
	
	if( this!=seat.getEvent())
		return 0.00;
	
	else {
		if (seat.getType()==Configuration.Type.ADVANCE_SALE) {
			Double total=(100-this.getDiscountAdvanceSale())*this.getPrice()/100;
			return total;
		}else {
			return this.getPrice();
			
		}	
	}
}
@Override
public Double getCollectionEvent() {
	// TODO Auto-generated method stub
	
	double price=0.0;
	
	for(int i=0; i<seats.length; i++ ) {
		if(seats[i]!=null) {
			price=price+this.getPrice(seats[i]);
		}
	}
	
	return price;
}


@Override
public int getPosPerson(Person p) {
	// TODO Auto-generated method stub
	
	int pos=-1;
	for(int i=0; i<seats.length && pos==-1 ; i++) 
		if(seats[i]!=null && seats[i].getHolder().equals(p)) 
			pos = i+1; 	
		
	return pos;
}


@Override
public boolean isAdvanceSale(Person p) {
	// TODO Auto-generated method stub
	
	if(this.getPosPerson(p) != -1) {
		if(seats[getPosPerson(p)-1].getType() == Configuration.Type.ADVANCE_SALE) {
			return true;
		}else {
			return false;
		}
	}
	
	else {
		return false;
	}
}
   


}	
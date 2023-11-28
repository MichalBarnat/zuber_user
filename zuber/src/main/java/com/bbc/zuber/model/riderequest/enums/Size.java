package com.bbc.zuber.model.riderequest.enums;

public enum Size {
    SINGLE,
    GROUP
}
 /*
 Tutaj warto dodać value, które będzie determinować ilość miejsc dostęponych w taxi.
 Zakładając, że auta osobowe to mogą być 4 miejsca jak np. dla S7 które może być taxi premium
 to wartość dla GROUP ustawimy na 8 żeby wymusić dostępność busa.
 Dodatkowo trzeba dla nich ustawić wartość żeby odpowiednio przejazd grupowy był droższy od single.
 Proponuje wartości: numberOfSeats oraz value czyli kolejno to będzie:
 SINGLE (numberOfSeats 1, value 1)
 GROUP (numberOfSeats 8, value 3)
  */
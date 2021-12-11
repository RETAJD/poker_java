				README !

URUCHOMIENIE KLIENTOW I SERVERA

	SERVER:
	Aby uruchomić server wchodzimy do klasy Serwer.java 
	uruchamiamy:   public static void main    - 
	po uruchomieniu powinno nam się wyświetlić w konsoli:
	
			Nowy serwer powstał
			Inicjalizacja
			Koniec Inicjalizacji
			Oczekiwanie na graczy
	KLIENT: 
		Aby uruchomić klientów wchodzimy do klasy Klient.java 
		uruchamiamy : public class Klient implements NetworkMessageReceiver 
		możemy uruchomić klika razy np. 2  - wtedy będziemy mieli dwóch graczy
	
		Po uruchomieniu klienta dostajemy informację: 
		"Podaj nazwe: "  - tutaj podajemy swoje username
		
		analogicznie na pozostalych klientach
		po wpisaniu nazw: dostajemy taki komunikat: 
		
		ID zostało ustawione na 118623726    - ustawione ID 
		Wpisz    Ready    żeby rozpocząć grę: - Polecenie które należy dalej wykonać
		Gracz zajął 2 miejsce przy stole	- Miejsce przy stole


ROZGRYWKA 
		
		Aby dołączyć do rozgrywki w pokera należy wpisać Ready na min 2 klientach
		po wpisaniu "Ready" dostajemy komunikat : Zadeklarowałeś gotowość do gry!
		
		Zadeklarowałeś gotowość do gry!
		Twój wkład wynosi 10    - zostaje pobrana opłata startowa - ante
		
		Teraz możemy zacząć rozgrywkę: 
		DOSTAJEMY KOMUNIKATY :
 
		Teraz wykonujesz ruch				- informacja że nasz ruch
 		 Wpisz Przebijam 20 aby przebić do 20    	-informacje o możliwosciach
		 Wpisz Sprawdzam aby sprawdzić
 	 	 Wpisz Pasuje aby spasować
		 Wpisz Czekam aby zaczekać
		Żeby wymienić niektóre karty wpisz np.  Swap: 6♦,A♥
		Uwaga, masz tylko jedną zamianę - wpisz wszystkie karty, które chcesz wymienić
		Twoja talia to: {6♥,4♠,6♦,9♦,A♥}

		Możemy na tym etapie też wymienić karty - robimy to tak : Swap: 6♦,A♥
		po przecinku wybieramy karty z ręki
		
		przechodzimy do następnego klienta i analogicznie
KONCOWE: 
		Kiedy wszyscy gracze sprawdzają LUB wszyscy spasują 
		to cała pula przechodzi do wygranego
		
		dostajemy komunikat np.:
	 
		
			Twój wkład wynosi 20
			Twoje saldo wynosi 980
			>Zwycięzcy to  <nazwauzytk>
			>Nagroda wynosi  40
			Twoje saldo wynosi 1020
			Rozpoczyna się nowa gra
		(logi wygenerowane w przypadku 2 graczy gdzie wygrana zostala przesadzona: jeden gracz miał parę
		drugi mial uklad najwyzsza karta) - wygral gracz z parą i dostał 40 żetonow
			




# EmbeddedSystemProgramming1415
___Fall Detector___
Design and implement an app that records data coming from the accelerometer in your device, and uses them to detect when the user of the device falls. When a “fall” event is detected, the app retrieves the location of the user and notifies a pre-established list of people by email. Fall detection for elderly people has been an area of active research in recent years. The app should manage and store multiple activity sessions; each session may contain zero, one or multiple “fall” events. The app should provide at least the following user interfaces (UIs) and functions.

A UI that lists all activity sessions (including the one currently in progress, if such a session exists). At least the following information must be displayed for each session:
- activity session name,
- starting date and time for the session,
- session duration,
- number of “fall” events detected in the session,
- a thumbnail picture that is unique for each session (a “signature” of the session, so to speak) and is generated from acceleration data recorded in the session. 
How the signature is generated is entirely left to the group. If a session is currently in progress, the corresponding list element must be distinguishable in some way (e.g., a red dot is shown inside it); it is not required to update the session duration in real time. When the user taps a list element, UI#2 (see below) is displayed unless the element is associated with the session in progress: in this case, UI#3 (see below) is displayed instead. The UI should also provide a way (button, menu, long-press contextual menu) to perform the following functions.
- Start a new session by displaying UI#3 to record new accelerometer data.
- Delete an existing session.
- Rename a session. 
No new session can be created if a session is already in progress.

A separate UI that shows the details of an activity session, and chiefly a list of “fall” events in the session. The following is a bare-minimum list of information that should be displayed:
- session name,
- thumbnail “signature” picture for the session,
- starting date and time for the session,
- session duration,
- a list of “fall” events. 
Each element in the list must display at least the following details:
- date and time when the event happened,
- whether a notification has been successfully sent or not. When an element of the list is tapped, UI#4 is displayed (see below) to show additional details. The UI should also provide a way to rename the session.

A separate UI to manage the currently active session. The UI should provide the following functions.
- Give a name to the session.
- Start the session. The UI continues to be shown while recording is in progress.
- Pause the session.
- Resume the session.
- End the session and leave this UI for UI#2. 
The UI should also display at least the same information specified for UI#2; the session duration and the list of fall events must be updated in real time. Furthermore, the UI must display a visual clue (bars, charts…), updated in real time as well, about the signal level coming from each of the 3 axes of the accelerometer. The recording process must continue when the app is moved to the background or the screen is turned off. As soon as a new “fall” event is detected, an email with the details about the event is sent to the pre-established list of people. How fall events are actually detected from accelerometer data is entirely left to the group.

A UI that shows the details about a “fall” event. The following is a bare-minimum list of information that should be displayed:
- thumbnail “signature” picture for the session associated with the event,
- date and time when the event happened,
- latitude and longitude of the user when the event happened,
- whether a notification has been successfully sent or not,
- 500 ms of accelerometer data before and after the event (total: 1 second of accelerometer data). 
Accelerometer data must be displayed in graphical form. It is not required that data is scrollable or zoomable.

A “Preferences” UI, implemented according to the interface guidelines of the Android platform. The following preferences are mandatory:
- sample rate used to record accelerometer data (see UI#3),
- maximum duration of a session (see UI#3),
- management of the list of people that must receive notification emails,
- definition of an alarm that reminds the user to start a new session every day at a given time. 
All UIs must be correctly displayed and profitably usable in both portrait and landscape mode. When appropriate, the layout of a UI must be different in portrait and landscape mode. For UI#3: if the group thinks that user movements during recording may cause the UI orientation to flip too frequently, a switch (UISwitch, ToggleButton, Switch) can be introduced to give the option of locking the orientation before the recording process starts. However, both orientations must be supported in UI#3 as well. UI#1 can be merged with UI#2 if the display is big enough (e.g., the application is run on a tablet computer). Error messages should be presented to the user when appropriate (e.g., when no more space is available for storing data, when a new session cannot be started, etc.). The state of the app must be fully preserved when the app itself loses the foreground. Recording of accelerometer data and detection of “fall” events must continue even when the app loses the foreground. Sophisticated processing functions may give points to the group: make arrangements with the instructor. Strategies to minimize battery consumption may give points to the group: make arrangements with the instructor. The introduction of a splash screen is discouraged; its presence will not give the group any points. Accelerometer data can be stored in any format the group sees fit. It is forbidden to use third-party libraries without explicit consent from the instructor. The names of buttons provided above are only for description purposes and they are not binding: a group can decide to show a different text, or display information in a graphical fashion instead.



___TRADOTTO___


Progettare e realizzare un app che registra dati provenienti dall'accelerometro nel dispositivo, e li utilizza per rilevare quando l'utente del dispositivo cade. Quando viene rilevato un evento di "caduta", l'applicazione recupera la posizione dell'utente e notifica un elenco prestabilito di persone via e-mail. Rilevamento di caduta per le persone anziane è stata un'area di ricerca attiva negli ultimi anni. L'applicazione dovrebbe gestire e archiviare più sessioni di attività; ogni sessione può contenere zero, uno o più eventi "caduta". L'applicazione deve fornire almeno le seguenti interfacce utente (UI) e funzioni.


1) Una UI che elenca tutte le sessioni di attività (compreso quello in corso, se tale sessione esiste). 
	Almeno le seguenti informazioni devono essere visualizzate per ogni sessione: 
		- nome della sessione di attività;
		- la data e l'ora per la sessione; 
		- la durata della sessione;
		- il numero di eventi "caduta" rilevato nella sessione;
		- una miniatura che è unico per ciascuna sessione (a partire " firma "della sessione, per così dire) ed è generato dai dati di accelerazione registrati nella sessione.Come la firma viene generata è interamente lasciata al gruppo.
	Se una sessione è attualmente in corso, l'elemento della lista corrispondente deve essere distinguibile in qualche modo (ad esempio, un punto rosso è mostrato al suo interno); non è necessario aggiornare la durata della sessione in tempo reale.
	Quando l'utente tocca un elemento nell'elenco, UI # 2 (vedi sotto) viene visualizzato a meno dell'elemento associato alla sessione in corso: in questo caso viene visualizzata la UI # 3 (vedere di seguito). 

	L'interfaccia utente dovrebbe inoltre fornire un modo (pulsante, menu, il menu contestuale premendo a lungo) per eseguire le seguenti funzioni:
	- Avviare una nuova sessione visualizzando UI # 3 per registrare nuovi dati dall'accelerometro.
	- Cancellare un una sessione esistente. 
	- Rinominare una sessione.

	Non può essere creata una sessione se una sessione è già in corso. (Singleton)


2) Una UI separata che mostra i dettagli di una sessione di attività, e soprattutto un elenco degli eventi "caduta" nella sessione. Ciò che segue è un elenco nudo minimo di informazioni che dovrebbe essere visualizzato: 
	- nome della sessione;
	- miniatura "firma" per la sessione;
	- la data e l'ora per la sessione;
	- la durata della sessione;
	- un elenco di eventi "caduta" di partenza.

	Ogni elemento della lista deve presentare almeno i seguenti dati: 
	- la data e l'ora in cui è accaduto l'evento;
	- se una segnalazione è stata inviata con successo o meno. 

	Quando un elemento della lista viene prelevata, viene visualizzato UI # 4 (vedi sotto) per visualizzare ulteriori dettagli.

	L'interfaccia utente dovrebbe anche fornire un modo per rinominare la sessione.

3) Una interfaccia utente separato per gestire la sessione attiva. L'interfaccia utente dovrebbe fornire le seguenti funzioni:
	- Dare un nome al session;
	- Avviare la sessione. L'interfaccia utente continua ad essere mostrata, mentre avviene la registrazione;
	- Mettere in pausa la session;
	- Riprendere la Session;
	- Terminare la sessione e lasciare questa interfaccia utente per andare in UI #2.
	
	L'interfaccia utente dovrebbe anche mostrare almeno le stesse informazioni specificate per UI # 2;
	La durata della sessione e l'elenco degli eventi caduta devono essere aggiornate in tempo reale.
	Inoltre, l'utente deve visualizzare un indizio visivo (barre, grafici ...), aggiornate in tempo reale, nonché, sul livello di segnale proveniente da ciascuno dei 3 assi dell'accelerometro.
	Il processo di registrazione deve continuare quando l'applicazione viene spostata sullo sfondo o lo schermo è spento.
	Non appena viene rilevato un nuovo evento "caduta", una e-mail con i dettagli circa l'evento viene inviato alla lista prestabilito di persone.
	Come eventi di caduta sono effettivamente rilevati dai dati dell'accelerometro è interamente lasciata al gruppo.

4) Una UI che mostra i dettagli di un evento "caduta".
	Quello che segue è un elenco nudo minimo di informazioni che dovrebbe essere visualizzato:
	- miniatura "firma" per la sessione associata all'evento;
	- la data e l'ora in cui è accaduto l'evento;
	- la latitudine e la longitudine della all'utente quando è accaduto l'evento;
	- sia una notifica è stato inviato con successo o meno;
	- 500 ms di dati accelerometrici prima e dopo l'evento (totale: 1 secondo di dati accelerometrici).
	
	Dati dell'accelerometro devono essere visualizzati in forma grafica. Non è necessario che i dati siano scrollabili o zoommabili.

5) Una UI "Preferenze", realizzato secondo le linee guida di interfaccia della piattaforma Android. 
	Le seguenti preferenze sono obbligatorie:
	- frequenza di campionamento utilizzato per registrare i dati dell'accelerometro (vedi UI # 3);
	- la durata massima di una sessione (vedi UI # 3);
	- la gestione della lista di persone che devono ricevere e-mail di notifica;
	- la definizione di un allarme che ricorda all'utente di iniziare una nuova sessione ogni giorno in un dato momento.
	
	
Tutte le interfacce utente devono essere visualizzate correttamente e proficuamente utilizzabili sia in modalità verticale e orizzontale.
Quando appropriato, il layout di una interfaccia utente deve essere diverso tra modalità verticale e quella orizzontale.

Per UI # 3: se il gruppo ritiene che i movimenti dell'utente durante la registrazione può causare l'orientamento UI per capovolgere troppo spesso, un interruttore (UISwitch, ToggleButton, Switch) può essere introdotto per dare la possibilità di bloccare l'orientamento prima che il processo di registrazione ha inizio. Tuttavia, entrambi gli orientamenti devono essere sostenuti in UI # 3.

UI # 1 può essere fusa con UI # 2 se il display è abbastanza grande (ad esempio, l'applicazione viene eseguita su un tablet). I messaggi di errore devono essere presentati all'utente quando appropriato (ad esempio, quando lo spazio disponibile per la memorizzazione di dati, quando una nuova sessione non può essere avviato, ecc).

Lo stato della applicazione deve essere completamente preservato quando l'applicazione non è in primo piano.

La registrazione dei dati accelerometrici e rilevamento di eventi "caduta" deve continuare anche 
quando l'applicazione non è più in primo piano.

Sofisticate funzioni di elaborazione possono dare punti al gruppo: prendere accordi con l'istruttore.

Strategie per ridurre al minimo il consumo della batteria possono dare punti al gruppo: prendere accordi con l'istruttore.

L'introduzione di una schermata di avvio è scoraggiato. La sua presenza non darà il gruppo di tutti i punti.

Dati accelerometrici possono essere memorizzati in qualsiasi formato il gruppo ritiene più opportuno.

E 'vietato l'uso di librerie di terze parti senza il consenso esplicito da parte del docente.

I nomi dei pulsanti descritti solo per la descrizione e non sono vincolanti: un gruppo può decidere di mostrare un testo diverso, o visualizzare le informazioni in modo grafico per esempio.


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

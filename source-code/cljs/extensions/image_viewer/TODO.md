
Legyen horizontal scroll galery.

Ha elkezdesz oldalirányba scrollolni (pl amikor mobilon egy overflow-x: scroll
 div-ben swipe-olsz: az is oldalirányu scroll), akkor x megtett pixel után
 preventDefault-olja a scroll eseményt és befejezi helyetted a scroll-t,
 tehát ugrik a köv. képre.

- A galéria controll-jai 3000 ms után eltünnek, és egérmozgásra jelennek meg ujra
 3000 ms-re

- Ha a kép kicsinyítve van akkor a kurzora legyen egy nagyito benne egy plusszal
  és zoomoljon bele

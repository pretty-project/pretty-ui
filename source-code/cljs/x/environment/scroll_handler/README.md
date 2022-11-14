
# WARNING#9904
- Az görgetés pozícióját és más görgetéssel kapcsolatos gyorsan változó
  adatot nem célszerű a Re-Frame adatbásisban tárolni, mivel az nem alkalmas
  a gyors egymás-utáni írások hatékony kezelésére, ugyanis minden Re-Frame
  adatbázis-írás következménye az összes aktív feliratkozás (subscription)
  újbóli kiértékelődése.

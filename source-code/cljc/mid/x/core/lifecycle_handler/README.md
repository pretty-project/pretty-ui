
# Az applikáció szerver-oldali életciklusai
# XXX#8870

- {:on-server-init ...}
  Az on-server-boot előtt lefutó események.

- {:on-server-boot ...}
  A szerver indítása előtt lefutó események.

- {:on-server-launch ...}
  A szerver indítása után lefutó események.



# Az applikáció kliens-oldali életciklusai
# XXX#8871

- {:on-app-init ...}
  Az on-app-boot előtt lefutó események.

- {:on-app-boot ...}
  Az applikáció első renderelése előtt lefutó események.

- {:on-app-launch ...}
  Az applikáció első renderelése után lefutó események.

- {:on-login ...}
  (Nem vendég) bejelentkezéskor lefutó események.

- {:on-browser-online ...}
  Nomen est omen.

- {:on-browser-offline ...}
  Nomen est omen.

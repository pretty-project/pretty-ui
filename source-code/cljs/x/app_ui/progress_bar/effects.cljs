
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.effects
    (:require [x.app-core.api               :as a :refer [r]]
              [x.app-ui.progress-bar.events :as progress-bar.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/simulate-process!
  ; @usage
  ;  [:ui/simulate-process!]
  (fn [{:keys [db]} _]
      ; A [:ui/simulate-process!] esemény megjelenít az állapotjelzőn egy hamis folyamatot.
      {:db             (r progress-bar.events/fake-process! db 100)
       :dispatch-later [{:ms 500 :dispatch [:ui/stop-faking-process!]}]}))

(a/reg-event-fx
  :ui/end-fake-process!
  ; @usage
  ;  [:ui/end-fake-process!]
  (fn [{:keys [db]} _]
      ; - Ha a [:ui/fake-process! ...] esemény által az állapotjelzőn szimulált hamis állapotot
      ;   nem fejezi be valós folyamat, akkor a [:ui/end-fake-process!] esemény használatával
      ;   befejezhető a szimulált hamis állapot.
      ;   Pl. kommunikációs hiba miatt nem történik meg az az esemény ami elindítaná azt a valós
      ;       folyamatot, ami befejezi a szimulált hamis állapotot.
      ;
      ; - A [:ui/end-fake-process!] esemény először 100%-ra állítja a szimulált hamis állapotot,
      ;   majd 500 ms késleltetéssel abba hagyja a hamis állapot szimulálását.
      ;
      ; - Ha a [:ui/end-fake-process!] esemény megtörténésekor NINCS szimulálva hamis állapot,
      ;   akkor az ... esemény nem végez műveletet.
      ;   Pl.: a [:ui/end-fake-process!] esemény megtörténése előtt a felhasználó elindít egy
      ;        folyamatot, ami befejezi a szimulált hamis állapotot, akkor a [:ui/end-fake-process!]
      ;        eseménynek már nem szabad újra szimulálnia a hamis állapot befejezését!
      (if-let [fake-progress (get-in db [:ui :progress-bar/meta-items :fake-progress])]
              {:db             (r progress-bar.events/fake-process! db 100)
               :dispatch-later [{:ms 500 :dispatch [:ui/stop-faking-process!]}]})))

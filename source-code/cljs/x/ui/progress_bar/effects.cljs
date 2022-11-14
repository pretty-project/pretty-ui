
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-bar.effects
    (:require [re-frame.api             :as r :refer [r]]
              [x.ui.progress-bar.events :as progress-bar.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/simulate-process!
  ; @param (map)(opt) process-props
  ;  {:on-process-ended (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:x.ui/simulate-process!]
  ;
  ; @usage
  ;  [:x.ui/simulate-process! {...}]
  (fn [{:keys [db]} [_ {:keys [on-process-ended]}]]
      ; A [:x.ui/simulate-process!] esemény megjelenít az állapotjelzőn egy hamis folyamatot.
      {:db             (r progress-bar.events/fake-process! db 100)
       :dispatch-later [{:ms 500 :dispatch [:x.ui/stop-faking-process!]}
                        {:ms 500 :dispatch on-process-ended}]}))

(r/reg-event-fx :x.ui/end-fake-process!
  ; @usage
  ;  [:x.ui/end-fake-process!]
  (fn [{:keys [db]} _]
      ; - Ha a [:x.ui/fake-process! ...] esemény által az állapotjelzőn szimulált hamis állapotot
      ;   nem fejezi be valós folyamat, akkor a [:x.ui/end-fake-process!] esemény használatával
      ;   befejezhető a szimulált hamis állapot.
      ;   Pl. kommunikációs hiba miatt nem történik meg az az esemény ami elindítaná azt a valós
      ;       folyamatot, ami befejezi a szimulált hamis állapotot.
      ;
      ; - A [:x.ui/end-fake-process!] esemény először 100%-ra állítja a szimulált hamis állapotot,
      ;   majd 500 ms késleltetéssel abba hagyja a hamis állapot szimulálását.
      ;
      ; - Ha a [:x.ui/end-fake-process!] esemény megtörténésekor NINCS szimulálva hamis állapot,
      ;   akkor az ... esemény nem végez műveletet.
      ;   Pl. a [:x.ui/end-fake-process!] esemény megtörténése előtt a felhasználó elindít egy
      ;        folyamatot, ami befejezi a szimulált hamis állapotot, akkor a [:x.ui/end-fake-process!]
      ;        eseménynek már nem szabad újra szimulálnia a hamis állapot befejezését!
      (if-let [fake-progress (get-in db [:x.ui :progress-bar/meta-items :fake-progress])]
              {:db             (r progress-bar.events/fake-process! db 100)
               :dispatch-later [{:ms 500 :dispatch [:x.ui/stop-faking-process!]}]})))

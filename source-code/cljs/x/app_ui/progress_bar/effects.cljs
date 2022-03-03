
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.effects
    (:require [x.app-core.api               :as a :refer [r]]
              [x.app-ui.progress-bar.events :as progress-bar.events]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/simulate-process!
  ; @usage
  ;  [:ui/simulate-process!]
  (fn [{:keys [db]} _]
      ; A [:ui/simulate-process!] esemény megjelenít az állapotjelzőn egy hamis folyamatot.
      {:db (r progress-bar.events/fake-process! db 100)
       :dispatch-later [{:ms 500 :dispatch [:ui/stop-faking-process!]}]}))

(a/reg-event-fx
  :ui/end-fake-process!
  ; @usage
  ;  [:ui/end-fake-process!]
  (fn [{:keys [db]} _]
      ; Ha a [:ui/fake-process! ...] esemény által az állapotjelzőn megjelenített hamis állapotot
      ; nem fejezi be valós folyamat, akkor a [:ui/end-fake-process!] esemény befejezi a hamis folyamatot.
      ; (pl. kommunikációs hiba miatt nem történik meg az az esemény ami elindítana egy valós folyamatot)
      {:db (r progress-bar.events/fake-process! db 100)
       :dispatch-later [{:ms 500 :dispatch [:ui/stop-faking-process!]}]}))

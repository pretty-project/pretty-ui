
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.effects
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.progress-bar.events :as progress-bar.events]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/simulate-process!
  ; @usage
  ;  [:ui/simulate-process!]
  (fn [{:keys [db]} _]
      {:db (r progress-bar.events/fake-process! db 100)
       :dispatch-later [{:ms 500 :dispatch [:ui/stop-faking-process!]}]}))

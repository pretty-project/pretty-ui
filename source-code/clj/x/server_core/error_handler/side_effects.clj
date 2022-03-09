
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.error-handler.side-effects
    (:require [x.app-details                      :as details]
              [x.server-core.event-handler        :as event-handler]
              [x.server-core.error-handler.engine :as error-handler.engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-warning!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) warning-message
  [& warning-message])
  ;(.warn js/console (reduce #(str %1 "\n" %2) nil warning-message)))

(defn print-error!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (list of strings) error-message
  [& error-message])
  ;(.error js/console (reduce #(str %1 "\n" %2) nil error-message)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/print-warning! print-warning!)

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/print-error! print-error!)

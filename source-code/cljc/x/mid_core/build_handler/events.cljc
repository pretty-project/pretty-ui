
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.build-handler.events
    (:require [x.mid-core.event-handler :as event-handler]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn store-app-build!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) app-build
  ;
  ; @return (string)
  [db [_ app-build]]
  (assoc-in db [:core :build-handler/meta-items :app-build] app-build))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-event-db :core/store-app-build! store-app-build!)

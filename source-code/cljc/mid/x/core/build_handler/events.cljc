
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.core.build-handler.events
    (:require [re-frame.api :as r]))



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
(r/reg-event-db :core/store-app-build! store-app-build!)

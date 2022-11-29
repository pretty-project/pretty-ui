
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

(defn store-build-version!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) build-version
  ;
  ; @return (string)
  [db [_ build-version]]
  (assoc-in db [:x.core :build-handler/meta-items :build-version] build-version))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.core/store-build-version! store-build-version!)


;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.subs
    (:require [mid-fruits.candy                      :refer [param return]]
              [re-frame.api                          :as r :refer [r]]
              [x.app-core.api                        :as core]
              [x.app-developer.developer-tools.state :as developer-tools.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db-write-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (swap!   developer-tools.state/DB-WRITE-COUNT inc)
  (return @developer-tools.state/DB-WRITE-COUNT))

(defn print-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [debug-mode (r core/get-debug-mode db)]
       (= debug-mode "pineapple-juice")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer/get-db-write-count get-db-write-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools/print-events? print-events?)

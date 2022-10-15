
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.subs
    (:require [developer-tools.core.state :as core.state]
              [mid-fruits.candy           :refer [return]]
              [re-frame.api               :as r :refer [r]]
              [x.app-core.api             :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db-write-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (swap!   core.state/DB-WRITE-COUNT inc)
  (return @core.state/DB-WRITE-COUNT))

(defn print-events?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (let [debug-mode (r core/get-debug-mode db)]
       (= debug-mode "pineapple-juice")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.core/get-db-write-count get-db-write-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.core/print-events? print-events?)

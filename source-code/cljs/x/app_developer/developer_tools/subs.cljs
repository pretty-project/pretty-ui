

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.developer-tools.subs
    (:require [mid-fruits.candy                      :refer [param return]]
              [x.app-core.api                        :as a :refer [r]]
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
  (let [debug-mode (r a/get-debug-mode db)]
       (= debug-mode "pineapple-juice")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer/get-db-write-count get-db-write-count)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :developer-tools/print-events? print-events?)

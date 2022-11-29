
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.subs
    (:require [candy.api                  :refer [return]]
              [developer-tools.core.state :as core.state]
              [re-frame.api               :as r]
              [x.core.api                 :as x.core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-db-write-count
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db _]
  (swap!   core.state/DB-WRITE-COUNT inc)
  (return @core.state/DB-WRITE-COUNT))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :developer-tools.core/get-db-write-count get-db-write-count)

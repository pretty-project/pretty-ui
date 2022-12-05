
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.subs
    (:require [engines.engine-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-engine-prop core.subs/get-engine-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lister-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ lister-id item-key]]
  (r get-engine-prop db lister-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-lister/get-lister-prop get-lister-prop)

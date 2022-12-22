
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
  ; @param (keyword) lister-id
  ; @param (keyword) item-key
  ;
  ; @usage
  ; (r get-lister-prop db :my-lister :my-prop)
  ;
  ; @return (map)
  [db [_ lister-id item-key]]
  (r get-engine-prop db lister-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
; [:item-lister/get-lister-prop :my-lister :my-prop]
(r/reg-sub :item-lister/get-lister-prop get-lister-prop)

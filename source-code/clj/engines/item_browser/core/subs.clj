
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.core.subs
    (:require [engines.engine-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.core.subs
(def get-engine-prop core.subs/get-engine-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-browser-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ browser-id item-key]]
  (r get-engine-prop db browser-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-browser/get-browser-prop get-browser-prop)

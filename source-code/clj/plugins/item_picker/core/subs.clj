
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-plugin-prop core.subs/get-plugin-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-picker-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ picker-id item-key]]
  (r get-plugin-prop db picker-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-picker/get-picker-prop get-picker-prop)

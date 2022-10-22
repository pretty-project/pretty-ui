
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [re-frame.api                     :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-plugin-prop core.subs/get-plugin-prop)



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
  (r get-plugin-prop db browser-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :item-browser/get-browser-prop get-browser-prop)

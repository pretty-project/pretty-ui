
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.popups.subs
    (:require [re-frame.api  :as r :refer [r]]
              [x.ui.renderer :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-popup-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (keyword) prop-key
  ;
  ; @return (boolean)
  [db [_ popup-id prop-key]]
  (r renderer/get-element-prop db :popups popup-id prop-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-popup-prop get-popup-prop)

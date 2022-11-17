
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.surface.subs
    (:require [re-frame.api       :as r :refer [r]]
              [x.ui.renderer.subs :as renderer.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-surface-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) surface-id
  ; @param (keyword) prop-key
  ;
  ; @return (boolean)
  [db [_ surface-id prop-key]]
  (r renderer.subs/get-element-prop db :surface surface-id prop-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-surface-prop get-surface-prop)

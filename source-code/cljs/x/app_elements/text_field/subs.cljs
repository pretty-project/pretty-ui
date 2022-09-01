
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.subs
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn surface-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [db [_ field-id]]
  (get-in db [:elements :element-handler/meta-items field-id :surface-visible?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.text-field/surface-visible? surface-visible?)


;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.subs
    (:require [x.app-core.api :as a]))




;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-option-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:value-path (vector)}
  ;
  ; @return (boolean)
  [db [_ button-id {:keys [value-path]}]]
  (let [value (get-in db value-path)]
       (-> value nil? not)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :elements.radio-button/any-option-selected? any-option-selected?)


;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.password-field.side-effects
    (:require [re-frame.api                        :as r]
              [x.app-elements.password-field.state :as password-field.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-password-visibility!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  [field-id]
  (swap! password-field.state/PASSWORD-VISIBILITY update field-id not))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements.password-field/toggle-password-visibility! toggle-password-visibility!)

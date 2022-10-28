
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element.side-effects
    (:require [re-frame.api                          :as r]
              [x.app-environment.api                 :as x.environment]
              [x.app-elements.target-handler.helpers :as target-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id target-handler.helpers/element-id->target-id x.environment/focus-element!))

(defn blur-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  [element-id]
  (-> element-id target-handler.helpers/element-id->target-id x.environment/blur-element!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements/focus-element! focus-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :elements/blur-element! blur-element!)


;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element.side-effects
    (:require [elements.target-handler.helpers :as target-handler.helpers]
              [re-frame.api                    :as r]
              [x.environment.api               :as x.environment]))



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


;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.6.2
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.deletable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.targetable :as targetable]
              [x.app-environment.api            :as environment]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name deletable
;  Olyan elem, amelyet csoportban megjelenítve lehetséges eltávolítani a csoportból.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn deletable-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-delete (metamorphic-event)}
  ;
  ; @return (map)
  ;  {:disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)
  ;   :title (metamorphic-content)}
  [element-id {:keys [disabled? on-delete]}]
  ; Az on-delete eseményt nem szükséges a más on-* kezdetű eseményekhez hasonlóan
  ; státusz-esemény használatával kezelni, mivel semmilyen adatbázis-változást
  ; és más esemény-meghívást nem hajt végre!
  ;
  ; :on-click [:elements/element-deleted element-id]
  ; =>
  ; :on-click #(a/dispatch on-delete)
  (if disabled? {:disabled     (param true)}
                {:on-click    #(a/dispatch on-delete)
                 :on-mouse-up #(environment/blur-element!)
                 :title        (components/content {:content :remove!})}))

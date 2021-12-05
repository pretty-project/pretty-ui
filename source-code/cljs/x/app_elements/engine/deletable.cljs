
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.5.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.deletable
    (:require [mid-fruits.candy                 :refer [param]]
              [mid-fruits.map                   :as map]
              [x.app-components.api             :as components]
              [x.app-core.api                   :as a :refer [r]]
              [x.app-elements.engine.element    :as element]
              [x.app-elements.engine.focusable  :as focusable]
              [x.app-elements.engine.targetable :as targetable]))



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
  ; :on-click [:elements/->element-deleted element-id]
  ; =>
  ; :on-click #(a/dispatch on-delete)
  (cond-> {} (boolean disabled?) (merge {:disabled true})
             (not     disabled?) (merge {:on-click   #(a/dispatch on-delete)
                                         :on-mouse-up (focusable/blur-element-function element-id)
                                         :title       (components/content {:content :remove!})})))

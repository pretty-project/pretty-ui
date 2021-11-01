
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.27
; Description:
; Version: v0.4.2
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.stated-element
    (:require [mid-fruits.map :as map]
              [x.app-components.api          :as components]
              [x.app-elements.engine.element :as element]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name constant-prop
;  Az egyes elemek azon tulajdonságai konstansak, amelyek eltárolódnak a Re-Frame adatbázisban.
;
;  A XXX#0069 logika szerint az elem számára a konstans tulajdonságainak forrása
;  a Re-Frame adatbázis ezért azok az elem paraméteit ért külső hatásra nem változnak.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
;  Az elem mely tulajdonságai kerüljenek a Re-Frame adatbázisba
(def CONSTANT-PROPS-KEYS
     [:autoclear? :default-value :disallow-empty-input-group? :emptiable? :form-id
      :keypress :get-label-f :get-value-f :group-id :initial-options :initial-value
      :input-ids :max-input-count :min-input-count :listen-to-change? :on-blur
      :on-change :on-check :on-click :on-delete :on-empty :on-enter :on-extend
      :on-focus :on-reset :on-select :on-uncheck :options-path :required? :validator
      :value-path])



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props->initial-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) element-props
  ;  {...}
  ;
  ; @return (map)
  ;  {...}
  [base-props]
  (map/inherit base-props CONSTANT-PROPS-KEYS))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:component (component)
  ;   :element-props (map)
  ;    {:disabler (subscription vector)(opt)}
  ;   :destructor (metamorphic-event)(opt)
  ;   :initializer (metamorphic-event)(opt)
  ;   :modifier (function)(opt)
  ;   :subscriber (subscription vector)(opt)
  ;   :updater (metamorphic-event)(opt)}
  ;
  ; @return (component)
  [element-id {:keys [component destructor disabler element-props initial-props
                      initializer modifier subscriber updater] :as context-props}]
  (let [disabler           (get element-props disabler)
        initial-props      (element-props->initial-props element-props)
        element-props-path (element/element-props-path   element-id)]
       [components/stated element-id
         {:base-props         element-props
          :component          component
          :destructor         destructor
          :disabler           disabler
          :initial-props      initial-props
          :initial-props-path element-props-path
          :initializer        initializer
          :modifier           modifier
          :subscriber         subscriber
          :updater            updater}]))

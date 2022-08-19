
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.27
; Description:
; Version: v0.4.2
; Compatibility: x4.4.8




;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.stated-element
    (:require [x.app-components.api          :as components]
              [x.app-db.api                  :as db]
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
;
; Az :on-* kezdetű eseményeket leíró tulajdonságok közül nem mindnél szükséges az értéket
; a Re-Frame adatbázisban tárolni. Az átláthatóság, karbantarthatóság és konzisztencia megtartása
; miatt azonban minden :on-* kezdetű tulajdonság konstans tulajdonságként van használva.
(def CONSTANT-PROPS-KEYS
     [:autoclear? :auto-focus? :default-value :disallow-empty-input-group? :emptiable?
      :form-id :keypress :get-label-f :get-value-f :group-id :initial-options
      :initial-value :input-ids :max-input-count :min-input-count :modifier :listen-to-change?
      :on-blur :on-change :on-check :on-click :on-delete :on-empty :on-enter
      :on-focus :on-reset :on-select :on-type-ended :on-uncheck :options-path
      :required? :validator :value-path])



;; -- Helpers -----------------------------------------------------------------
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
  (select-keys base-props CONSTANT-PROPS-KEYS))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) context-props
  ;  {:element-props (map)
  ;    {:disabler (subscription-vector)(opt)}
  ;   :initializer (metamorphic-event)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)
  ;   :subscriber (subscription-vector)(opt)}
  [element-id {:keys [destructor element-props initial-props initializer modifier render-f subscriber]
               :as context-props}]
  (let [initial-props      (element-props->initial-props element-props)
        element-props-path (db/path :elements/primary element-id)]
       [components/stated element-id
                          {:base-props         element-props
                           :destructor         destructor
                           :initial-props      initial-props
                           :initial-props-path element-props-path
                           :initializer        initializer
                           :modifier           modifier
                           :render-f           render-f
                           :subscriber         subscriber}]))

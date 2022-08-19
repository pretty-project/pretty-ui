

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns reagent.api
    (:require [reagent.atom       :as atom]
              [reagent.core       :as core]
              [reagent.dom        :as dom]
              [reagent.helpers    :as helpers]
              [reagent.lifecycles :as lifecycles]
              [reagent.references :as references]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; reagent.atom
(def not!  atom/not!)
(def ratom atom/ratom)

; reagent.core
(def adapt-react-class core/adapt-react-class)
(def as-element        core/as-element)

; reagent.dom
(def render dom/render)

; reagent.helpers
(def component? helpers/component?)

; reagent.lifecycles
(def lifecycles lifecycles/lifecycles)

; reagent.references
(def arguments references/arguments)

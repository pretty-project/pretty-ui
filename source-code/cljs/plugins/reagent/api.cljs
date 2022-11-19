
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.reagent.api
    (:require [plugins.reagent.atom       :as atom]
              [plugins.reagent.helpers    :as helpers]
              [plugins.reagent.lifecycles :as lifecycles]
              [plugins.reagent.references :as references]
              [reagent.core               :as core]
              [reagent.dom                :as dom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.reagent.atom
(def not!  atom/not!)
(def ratom atom/ratom)

; plugins.reagent.helpers
(def component? helpers/component?)

; plugins.reagent.lifecycles
(def lifecycles lifecycles/lifecycles)

; plugins.reagent.references
(def arguments references/arguments)

; reagent.core
(def adapt-react-class core/adapt-react-class)
(def after-render      core/after-render)
(def as-element        core/as-element)

; reagent.dom
(def render dom/render)

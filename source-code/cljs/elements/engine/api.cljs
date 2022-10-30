
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.engine.api
    (:require [elements.engine.clickable      :as clickable]
              [elements.engine.element        :as element]
              [elements.engine.element-badge  :as element-badge]
              [elements.engine.input-group    :as input-group]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.engine.clickable
(def clickable-body-attributes clickable/clickable-body-attributes)

; x.app-elements.engine.element
(def element-attributes         element/element-attributes)
(def get-element-prop           element/get-element-prop)
(def get-element-props          element/get-element-props)
(def set-element-prop!          element/set-element-prop!)
(def update-element-prop!       element/update-element-prop!)
(def remove-element-prop!       element/remove-element-prop!)

; x.app-elements.engine.element-badge
(def element-badge element-badge/element-badge)

; x.app-elements.engine.input-group
(def get-input-group-value input-group/get-input-group-value)
(def get-input-group-props input-group/get-input-group-props)
(def decrease-input-count! input-group/decrease-input-count!)
(def increase-input-count! input-group/increase-input-count!)

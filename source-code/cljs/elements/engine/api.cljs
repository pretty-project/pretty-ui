
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
              [elements.engine.element-badge  :as element-badge]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; elements.engine.clickable
(def clickable-body-attributes clickable/clickable-body-attributes)

; elements.engine.element
(def element-attributes         element/element-attributes)
(def get-element-prop           element/get-element-prop)
(def get-element-props          element/get-element-props)
(def set-element-prop!          element/set-element-prop!)
(def update-element-prop!       element/update-element-prop!)
(def remove-element-prop!       element/remove-element-prop!)

; elements.engine.element-badge
(def element-badge element-badge/element-badge)

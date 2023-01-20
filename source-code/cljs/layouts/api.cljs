
(ns layouts.api
    (:require [layouts.box-popup.views     :as box-popup.views]
              [layouts.plain-popup.views   :as plain-popup.views]
              [layouts.plain-surface.views :as plain-surface.views]
              [layouts.struct-popup.views  :as struct-popup.views]
              [layouts.sidebar.views       :as sidebar.views]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; layouts.box-popup.views
(def box-popup box-popup.views/layout)

; layouts.plain-popup.views
(def plain-popup plain-popup.views/layout)

; layouts.plain-surface.views
(def plain-surface plain-surface.views/layout)

; layouts.struct-popup.views
(def struct-popup struct-popup.views/layout)

; layouts.sidebar.views
(def sidebar sidebar.views/layout)

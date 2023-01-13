
(ns layouts.api
    (:require [layouts.popup-a.views   :as popup-a.views]
              [layouts.popup-b.views   :as popup-b.views]
              [layouts.sidebar-b.views :as sidebar-b.views]
              [layouts.surface-a.views :as surface-a.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; layouts.popup-a.views
(def popup-a popup-a.views/layout)

; layouts.popup-b.views
(def popup-b popup-b.views/layout)

; layouts.sidebar-b.views
(def sidebar-b sidebar-b.views/layout)

; layouts.surface-a.views
(def surface-a surface-a.views/layout)

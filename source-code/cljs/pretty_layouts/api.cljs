
(ns pretty-layouts.api
    (:require [pretty-layouts.box-popup.views     :as box-popup.views]
              [pretty-layouts.plain-popup.views   :as plain-popup.views]
              [pretty-layouts.plain-surface.views :as plain-surface.views]
              [pretty-layouts.sidebar.views       :as sidebar.views]
              [pretty-layouts.struct-popup.views  :as struct-popup.views]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pretty-layouts.box-popup.views
(def box-popup box-popup.views/layout)

; pretty-layouts.plain-popup.views
(def plain-popup plain-popup.views/layout)

; pretty-layouts.plain-surface.views
(def plain-surface plain-surface.views/layout)

; pretty-layouts.struct-popup.views
(def struct-popup struct-popup.views/layout)

; pretty-layouts.sidebar.views
(def sidebar sidebar.views/layout)

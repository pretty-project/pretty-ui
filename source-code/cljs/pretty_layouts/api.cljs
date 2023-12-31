
(ns pretty-layouts.api
    (:require [pretty-layouts.box-popup.views     :as box-popup.views]
              [pretty-layouts.plain-popup.views   :as plain-popup.views]
              [pretty-layouts.plain-surface.views :as plain-surface.views]
              [pretty-layouts.sidebar.views       :as sidebar.views]
              [pretty-layouts.struct-popup.views  :as struct-popup.views]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/layout)
(def box-popup     box-popup.views/layout)
(def plain-popup   plain-popup.views/layout)
(def plain-surface plain-surface.views/layout)
(def struct-popup  struct-popup.views/layout)
(def sidebar       sidebar.views/layout)

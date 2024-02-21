
(ns pretty-layouts.api
    (:require [pretty-layouts.box-popup.views     :as box-popup.views]
              [pretty-layouts.plain-popup.views   :as plain-popup.views]
              [pretty-layouts.plain-surface.views :as plain-surface.views]
              [pretty-layouts.sidebar.views       :as sidebar.views]
              [pretty-layouts.popup.views  :as popup.views]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def box-popup     box-popup.views/view)
(def plain-popup   plain-popup.views/view)
(def plain-surface plain-surface.views/view)
(def popup         popup.views/view)
(def sidebar       sidebar.views/view)

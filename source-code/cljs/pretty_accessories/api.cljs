
(ns pretty-accessories.api
    (:require [pretty-accessories.badge.views :as badge.views]
              [pretty-accessories.bullet.views   :as bullet.views]
              [pretty-accessories.cover.views  :as cover.views]
              [pretty-accessories.marker.views  :as marker.views]
              [pretty-accessories.tooltip.views  :as tooltip.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def badge   badge.views/view)
(def bullet  bullet.views/view)
(def cover   cover.views/view)
(def marker  marker.views/view)
(def tooltip tooltip.views/view)

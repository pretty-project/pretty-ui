
(ns pretty-diagrams.api
    (:require [pretty-diagrams.circle-diagram.views :as circle-diagram.views]
              [pretty-diagrams.line-diagram.views   :as line-diagram.views]
              [pretty-diagrams.point-diagram.views  :as point-diagram.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/diagram)
(def line-diagram   line-diagram.views/diagram)
(def circle-diagram circle-diagram.views/diagram)

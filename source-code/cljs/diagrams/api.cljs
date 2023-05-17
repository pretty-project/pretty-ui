
(ns diagrams.api
    (:require [diagrams.circle-diagram.views :as circle-diagram.views]
              [diagrams.line-diagram.views   :as line-diagram.views]
              [diagrams.point-diagram.views  :as point-diagram.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; diagrams.*
(def line-diagram   line-diagram.views/diagram)
(def point-diagram  point-diagram.views/diagram)
(def circle-diagram circle-diagram.views/diagram)

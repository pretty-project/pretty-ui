
(ns pretty-diagrams.api
    (:require [pretty-diagrams.circle-diagram.views :as circle-diagram.views]
              [pretty-diagrams.line-diagram.views   :as line-diagram.views]
              [pretty-diagrams.point-diagram.views  :as point-diagram.views]
              [pretty-diagrams.bar-diagram.views  :as bar-diagram.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def bar-diagram    bar-diagram.views/view)
(def line-diagram   line-diagram.views/view)
(def circle-diagram circle-diagram.views/view)

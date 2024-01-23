
(ns pretty-engine.diagram.state.side-effects
    (:require [pretty-engine.element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.state.side-effects/*)
(def update-all-diagram-state! pretty-engine.element.state.side-effects/update-all-element-state!)
(def update-diagram-state!     pretty-engine.element.state.side-effects/update-element-state!)
(def clear-diagram-state!      pretty-engine.element.state.side-effects/clear-element-state!)

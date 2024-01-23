
(ns pretty-engine.input.state.side-effects
    (:require [pretty-engine.element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.state.side-effects/*)
(def update-all-input-state! pretty-engine.element.state.side-effects/update-all-element-state!)
(def update-input-state!     pretty-engine.element.state.side-effects/update-element-state!)
(def clear-input-state!      pretty-engine.element.state.side-effects/clear-element-state!)

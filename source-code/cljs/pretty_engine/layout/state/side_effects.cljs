
(ns pretty-engine.layout.state.side-effects
    (:require [pretty-engine.element.state.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.state.side-effects/*)
(def update-all-layout-state! pretty-engine.element.state.side-effects/update-all-element-state!)
(def update-layout-state!     pretty-engine.element.state.side-effects/update-element-state!)
(def clear-layout-state!      pretty-engine.element.state.side-effects/clear-element-state!)

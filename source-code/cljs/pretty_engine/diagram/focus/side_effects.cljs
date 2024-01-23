
(ns pretty-engine.diagram.focus.side-effects
    (:require [pretty-engine.element.focus.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.focus.side-effects/*)
(def focus-diagram!             pretty-engine.element.focus.side-effects/focus-element!)
(def blur-diagram!              pretty-engine.element.focus.side-effects/blur-element!)
(def mark-diagram-as-focused!   pretty-engine.element.focus.side-effects/mark-element-as-focused!)
(def unmark-diagram-as-focused! pretty-engine.element.focus.side-effects/unmark-element-as-focused!)
(def diagram-focused            pretty-engine.element.focus.side-effects/element-focused)
(def diagram-left               pretty-engine.element.focus.side-effects/element-left)


(ns pretty-engine.layout.focus.side-effects
    (:require [pretty-engine.element.focus.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.focus.side-effects/*)
(def focus-layout!             pretty-engine.element.focus.side-effects/focus-element!)
(def blur-layout!              pretty-engine.element.focus.side-effects/blur-element!)
(def mark-layout-as-focused!   pretty-engine.element.focus.side-effects/mark-element-as-focused!)
(def unmark-layout-as-focused! pretty-engine.element.focus.side-effects/unmark-element-as-focused!)
(def layout-focused            pretty-engine.element.focus.side-effects/element-focused)
(def layout-left               pretty-engine.element.focus.side-effects/element-left)

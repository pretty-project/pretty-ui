
(ns pretty-engine.diagram.lifecycles.side-effects
    (:require [pretty-engine.element.lifecycles.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.lifecycles.side-effects/*)
(def diagram-did-mount    pretty-engine.element.lifecycles.side-effects/element-did-mount)
(def diagram-will-unmount pretty-engine.element.lifecycles.side-effects/element-will-unmount)


(ns pretty-engine.layout.lifecycles.side-effects
    (:require [pretty-engine.element.lifecycles.side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-engine.element.lifecycles.side-effects/*)
(def layout-did-mount    pretty-engine.element.lifecycles.side-effects/element-did-mount)
(def layout-will-unmount pretty-engine.element.lifecycles.side-effects/element-will-unmount)

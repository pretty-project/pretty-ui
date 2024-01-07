
(ns pretty-build-kit.api
    (:require [pretty-build-kit.prototypes :as prototypes]
              [pretty-build-kit.default-props :as default-props]
              [pretty-build-kit.attributes :as attributes]
              [pretty-build-kit.adaptive :as adaptive]
              [pretty-build-kit.side-effects :as side-effects]
              [pretty-build-kit.effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-build-kit.adaptive/*)
(def adaptive-border-radius adaptive/adaptive-border-radius)
(def adaptive-text-height   adaptive/adaptive-text-height)

; @redirect (pretty-build-kit.attributes/*)
(def border-radius-attributes    attributes/border-radius-attributes)
(def border-attributes           attributes/border-attributes)
(def block-max-size-attributes   attributes/block-max-size-attributes)
(def block-min-size-attributes   attributes/block-min-size-attributes)
(def block-size-attributes       attributes/block-size-attributes)
(def content-max-size-attributes attributes/content-max-size-attributes)
(def content-min-size-attributes attributes/content-min-size-attributes)
(def content-size-attributes     attributes/content-size-attributes)
(def element-max-size-attributes attributes/element-max-size-attributes)
(def element-min-size-attributes attributes/element-min-size-attributes)
(def element-size-attributes     attributes/element-size-attributes)
(def wrapper-size-attributes     attributes/wrapper-size-attributes)
(def thumbnail-size-attributes   attributes/thumbnail-size-attributes)
(def indent-attributes           attributes/indent-attributes)
(def outdent-attributes          attributes/outdent-attributes)
(def column-attributes           attributes/column-attributes)
(def row-attributes              attributes/row-attributes)
(def color-attributes            attributes/color-attributes)
(def cursor-attributes           attributes/cursor-attributes)
(def font-attributes             attributes/font-attributes)
(def icon-attributes             attributes/icon-attributes)
(def text-attributes             attributes/text-attributes)
(def class-attributes            attributes/class-attributes)
(def effect-attributes           attributes/effect-attributes)
(def link-attributes             attributes/link-attributes)
(def state-attributes            attributes/state-attributes)
(def badge-attributes            attributes/badge-attributes)
(def bubble-attributes           attributes/bubble-attributes)
(def marker-attributes           attributes/marker-attributes)
(def progress-attributes         attributes/progress-attributes)
(def tooltip-attributes          attributes/tooltip-attributes)
(def mouse-event-attributes      attributes/mouse-event-attributes)

; @redirect (pretty-build-kit.default-props/*)
(def DEFAULT-HEIGHT     default-props/DEFAULT-HEIGHT)
(def DEFAULT-WIDTH      default-props/DEFAULT-WIDTH)
(def DEFAULT-DIMENSIONS default-props/DEFAULT-DIMENSIONS)

; @redirect (pretty-build-kit.prototypes/*)
(def default-values      prototypes/default-values)
(def default-value-group prototypes/default-value-group)
(def forced-values       prototypes/forced-values)
(def forced-value-group  prototypes/forced-value-group)
(def value-update-fns    prototypes/value-update-fns)
(def value-wrap-fns      prototypes/value-wrap-fns)

; @redirect (pretty-build-kit.side-effects/*)
(def dispatch-event-handler!      side-effects/dispatch-event-handler!)
(def dispatch-sync-event-handler! side-effects/dispatch-sync-event-handler!)

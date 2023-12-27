
(ns pretty-build-kit.api
    (:require [pretty-build-kit.prototypes :as prototypes]
              [pretty-build-kit.default-props :as default-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
